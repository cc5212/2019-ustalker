import asyncio
import aiohttp
import csv
from bs4 import BeautifulSoup

async def download_page(session, url, csv_writer):
  """
  Esta funcion recibe la sesion (que deberia estar logueada), la url y
  una wea pa escribir en un archivo, baja la pagina y la escribe en el archivo.

  PUM que sorpresa, no me lo esperaba.
  """
  async with session.get(url) as response:
    # por ahora voy a probar solo con example.com y me se donde esta el texto
    soup = BeautifulSoup(await response.text(), features="html5lib")
    strings = soup.body.div.stripped_strings
    csv_writer.writerow([' '.join(strings)])


async def download_batch(session, batch, csv_writer):
  tasks = []
  for url in batch:
    # print(f'\tDescargando url: {url}')
    task = asyncio.ensure_future(download_page(session, url, csv_writer))
    tasks.append(task)
  await asyncio.gather(*tasks)


async def download_all(batches, csv_writer):
  async with aiohttp.ClientSession() as session:
    # conectar a cuent ade ucursos aqui
    for i, batch in enumerate(batches):
      print(f'Descargando batch {i}')
      await download_batch(session, batch, csv_writer)
    


if __name__ == '__main__':
  # N es la cantidad de paginas que se quiere descargar (el ultimo offset)
  N = 10000
  # M es la cantidad de requests que se quieren hacer de una
  # WARNING: CUIDADO CON HACER ESTO MUY GRANDE, PUEDE QUEDAR LA CAGADA
  M = 500
  print(f'Cantidad total de requests: {N}')
  print(f'Cantidad de requests a la vez: {M}')
  print(f'Numero de batches: {(N + M - 1) // M}')
  print(f'\nAfirmense cabros...\n')


  # url base, los parentesis son pa puro quede mas bonito el codigo
  # base_url = (
  #   'https://www.u-cursos.cl/ingenieria/2/foro_institucion/'
  #   '?id_tema=&offset={}'
  # )
  base_url = 'https://example.com/{}'

  # esta wea vuelve un generator pa todas las url que queremos descargar,
  # si fuera un lista normal pesaria como 100kb lo que no es mucho pero
  # igual es sacrilegio
  batches = (
    (
      base_url.format(j)
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
  with open('out.tsv', 'w') as f:
    writer = csv.writer(f)
    asyncio.get_event_loop().run_until_complete(
      download_all(batches, writer)
    )
    
