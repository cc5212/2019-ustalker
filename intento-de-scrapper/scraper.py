import asyncio
import aiohttp
import pickle
import csv
from bs4 import BeautifulSoup
import re
import argparse
import sys
import getpass
import time

def parse_arguments():
  parser = argparse.ArgumentParser(
    description=(
      'Descarga las paginas [START, FINISH) del foro de la facultad.\n'
      'El tamanno default del batch es 10, tener cuidado con este parametro '
      'porque hacerlo muy grande puede hacer que bloqueen la cuenta.\n'
      'Leer el readme para una descripcion mas detrallada de uso y requisitos.'
      'Los archivos de salida se generan automaticamente y se llaman root_START-FINISH.tsv'
      'y child_START-FINISH.tsv'
    )
  )

  parser.add_argument("start", metavar="START", help="primera pagina que se quiere bajar",
                      type=int)
  parser.add_argument("finish", metavar="FINISH", help="ultima pagina que se quiere bajar",
                      type=int)
  parser.add_argument("-b", "--batch_size", default=10, help="cantidad de paginas que se bajan a la vez, default 10",
                      type=int)
  parser.add_argument("-l", "--login_data", help="un pickle con los datos del usuario para realizar la conexion, si se omite el script pide login")

  args = parser.parse_args()
  return args


def extract_data(raw_html):
  """
  Esta wea devuelve un diccionario y una lista. El diccionario tiene las weas
  que vamos a guardar del OP y la lista contiene diccionarios con la info
  que vamos a guardar en cada comentario hijo de la publicacion
  """
  soup = BeautifulSoup(re.sub(r'>\s+<', '><', raw_html), features='html5lib')


  # para el OP
  raices = soup.find_all('div', class_='raiz')
  roots = []
  for raiz in raices:
    temp = {}
    temp['id'] = raiz.attrs['id'].split('_')[1]
    temp['titulo'] = raiz.h1.getText(strip=True)
    temp['autor'] = (
      raiz.find('a', class_='usuario').getText(strip=True) 
      if raiz.find('a', class_='usuario') is not None
      else "NO_AUTHOR"
    )
    temp['fecha'] = raiz.find('li', class_='fecha').getText(strip=True)
    temp['tema'] = raiz.find('li', class_='tema').a.getText(strip=True)
    # para sacar el texto de un comentario hay que eliminar la lista
    # de botones que tiene al final, como responder, padre, etc.
    comentario = raiz.find('div', class_='texto')
    # cuidado que esto modifica la sopa, el ul se borra definitivamente
    comentario.ul.decompose()
    text = ' '.join(comentario.stripped_strings)
    temp['mensaje'] = text if len(text) > 0 else 'NO_TEXT'
    temp['current_time'] = time.time()
    roots.append(temp)

  hijos = soup.find_all('div', class_='hijo')
  childs = []
  for hijo in hijos:
    temp = {}
    temp['id'] = hijo.attrs['id'].split('_')[1]
    temp['id_th'] = hijo.attrs['class'][1][1:]
    temp['id_p'] = hijo.parent.attrs['id'].split('_')[1]
    temp['autor'] = (
      hijo.find('a', class_='usuario').getText(strip=True) 
      if hijo.find('a', class_='usuario') is not None
      else "NO_AUTHOR"
    )
    temp['fecha'] = hijo.find('em').getText(strip=True)

    # mismos comentarios que arriba
    comentario = hijo.find('div', class_='texto')
    comentario.ul.decompose()
    text = ' '.join(comentario.stripped_strings)
    temp['mensaje'] = text if len(text) > 0 else 'NO_TEXT'
    temp['current_time'] = time.time()
    childs.append(temp)

  return roots, childs


# async def fetch(session, url):
#     async with session.get(url) as response:
#         return await response.text()


async def download_page(session, url, root_writer, child_writer):
  """
  Esta funcion recibe la sesion (que deberia estar logueada), la url y
  una wea pa escribir en un archivo, baja la pagina y la escribe en el archivo.

  PUM que sorpresa, no me lo esperaba.
  """
  async with session.get(url) as response:
    # por ahora voy a probar solo con example.com y me se donde esta el texto
    # print(f'\t{url}')
    roots, childs = extract_data(await response.text())
    for root in roots:
      root_writer.writerow(root)

    for child in childs:
      child_writer.writerow(child)
    

async def download_batch(session, batch, root_writer, child_writer):
  tasks = []
  for i, url in enumerate(batch):
    if i is 0:
      print(f'\tPrimera url del batch: {url}')

    task = asyncio.ensure_future(
      download_page(session, url, root_writer, child_writer)
    )
    tasks.append(task)
  await asyncio.gather(*tasks)


async def download_all(batches, root_writer, child_writer, login_data):
  async with aiohttp.ClientSession() as session:
    # conectar a cuenta de ucursos aqui, si no se pasa un archivo
    # el script pide login
    # tengo mis datos escondidos, porque obvio
    if login_data:
      with open('user_data.pic', 'rb') as f:
        payload = pickle.load(f)
    else:
      payload = {}
      payload['username'] = input('Nombre de usuario: ')
      payload['password'] = getpass.getpass('Contrasenna (tranqui no se muestra): ')

    # es importante agregarle esto a la wea que se envia pa poder loguearse
    payload['servicio'] = 'ucursos'
    # payload['debug'] = 0

    # esta wea es a logearse con el usuario de cada uno y mantener la sesion
    # abierta pa poder seguir SURFEANDO ucursos
    post_url = 'https://www.u-cursos.cl/upasaporte/adi'

    async with session.post(post_url, data=payload) as resp:
      print(f"Hola, {payload['username'].split('.')[0].capitalize()} !")
      print('Respuesta login: ', resp.status)
      print()
      assert resp.status == 200, 'diablos, deberia ser 200'
    
    for i, batch in enumerate(batches):
      print(f'Descargando batch {i}')
      await download_batch(session, batch, root_writer, child_writer)


if __name__ == '__main__':
  args = parse_arguments()
  # print(args)
  # sys.exit()
  # N es la cantidad de paginas que se quiere descargar (el ultimo offset)
  N = args.finish - args.start
  # M es la cantidad de requests que se quieren hacer de una
  # WARNING: CUIDADO CON HACER ESTO MUY GRANDE, PUEDE QUEDAR LA CAGADA
  M = args.batch_size

  print(f'Cantidad total de requests: {N}')
  print(f'Cantidad de requests a la vez: {M}')
  print(f'Numero de batches: {(N + M - 1) // M}')
  print(f'\nAfirmense cabros...\n')


  # url base, los parentesis son pa puro quede mas bonito el codigo
  base_url = (
    'https://www.u-cursos.cl/ingenieria/2/foro_institucion/'
    '?id_tema=&offset={}'
  )
  # base_url = 'https://example.com/{}'

  # esta wea vuelve un generator pa todas las url que queremos descargar,
  # si fuera un lista normal pesaria como 100kb lo que no es mucho pero
  # igual es sacrilegio
  batches = (
    (
      base_url.format(args.start + j)
      for j
      in range(
        i * M,
        (i + 1) * M if (i + 1) * M < N else N
      )
    )
    for i
    in range((N + M - 1) // M)
  )

  # ahora empieza el mambo con I/O
  with open(f'root_{args.start}-{args.finish}.tsv', 'w') as f_root,\
      open(f'child_{args.start}-{args.finish}.tsv', 'w') as f_child:
    root_fields = ['id', 'titulo', 'autor', 'fecha', 'tema', 'mensaje', 'current_time']
    root_writer = csv.DictWriter(
      f_root,
      fieldnames=root_fields,
      delimiter='\t'
    )
    # mejor no escribir el header, para que sea mas facil unir
    # los archivos usando cat
    # root_writer.writeheader()
    
    child_fields = ['id', 'id_th', 'id_p', 'autor', 'fecha', 'mensaje', 'current_time']
    child_writer = csv.DictWriter(
      f_child,
      fieldnames=child_fields,
      delimiter='\t'
    )
    # mismo comentario de mas arriba
    # child_writer.writeheader()
    
    asyncio.get_event_loop().run_until_complete(
      download_all(batches, root_writer, child_writer, args.login_data)
    )
    print()
    print("Creo que termine, igual revisa que la cantidad de comentarios descargados tenga sentido")
    
