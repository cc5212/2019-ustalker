import requests
import pickle
from bs4 import BeautifulSoup

# tengo mis datos escondidos, porque obvio
with open('user_data.pic', 'rb') as f:
  payload = pickle.load(f)

# es importante agregarle esto a la wea que se envia pa poder loguearse
payload['servicio'] = 'ucursos'
# payload['debug'] = 0

# esta wea es a logearse con el usuario de cada uno y mantener la sesion
# abierta pa poder seguir SURFEANDO ucursos
post_url = 'https://www.u-cursos.cl/upasaporte/adi'
# print(payload)
session = requests.Session()
response = session.post(post_url, data=payload)
print(response.status_code)
assert response.status_code == 200, 'diablos, deberia ser 200'


# ahora es pa empezar a traer comentarios
first_page = 'https://www.u-cursos.cl/ingenieria/2/foro_institucion/'
response = session.get(first_page)

# esto es pa parsear el html y poder buscar weas
soup = BeautifulSoup(response.text, features='html5lib')
comments = soup.find_all(
	lambda tag : tag.has_attr('class')
		and set(['msg', 'raiz']).issubset(tag['class'])
)
assert len(comments) != 0, \
  'diablos, no pille ningun mensaje'

print(f'Pille {len(comments)} mensajes raiz! Aca te van:')


for i, comment in enumerate(comments):
	print('{}. ======================================='.format(i + 1))
	print(comment.div.div.get_text().strip())
	print()
