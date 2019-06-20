import datetime as dt
import re
import os
import argparse
import csv

# no encontre los meses y no me dio paja escribirlos asi que estos son los messesss
mes = {
    'Enero' : 1,
    'Febrero' : 2,
    'Marzo' : 3,
    'Abril' : 4,
    'Mayo' : 5,
    'Junio' : 6,
    'Julio' : 7,
    'Agosto' : 8,
    'Septiembre' : 9,
    'Octubre' : 10,
    'Noviembre' : 11,
    'Diciembre' : 12
}


def relative_date_2_timestamp(date_query: dt.datetime, typed_date: str):
    """
    Aqui entran las fechas relativas a la fecha de consulta
    date_query: datetime de la fecha de query
    typed_date: una fecha que es relativa a la de la query
    """
    matchDate = re.match(r'.*, hace (.*) (.*)', typed_date)
    if(matchDate):
        if(matchDate.group(2) == 'mins'):
            date_timestamp = date_query - dt.timedelta(minutes = int(matchDate.group(1)))
        elif(matchDate.group(2) == 'segs'):
            date_timestamp = date_query - dt.timedelta(seconds = int(matchDate.group(1)))
    else:
        matchDate = re.match(r'(.*), a las (.*):(.*) hrs.',typed_date)
        if(matchDate.group(1) == 'Hoy'):
            date_timestamp = dt.datetime(
                date_query.year,
                date_query.month,
                date_query.day,
                int(matchDate.group(2)),
                int(matchDate.group(3))
                )
        elif(matchDate.group(1) == 'Ayer'): #restar un dia
            date_timestamp = dt.datetime(
                (date_query - dt.timedelta(days = 1)).year,
                (date_query - dt.timedelta(days = 1)).month,
                (date_query - dt.timedelta(days = 1)).day,
                int(matchDate.group(2)),
                int(matchDate.group(3))
                )
    return date_timestamp.timestamp()


def date_2_timestamp(date_query: dt.datetime, typed_date: str):
    """
    el timestamp nomasss
    en caso de encontrarse con un formato de fecha no especificado
    retorna NO_MATCHED_FORMAT
    para el caso de los años, se espera que sean todos sobre los 2000
    si hay uno anterior a esto se guardara como la suma con 1900 (por si fuera 92 o algo asi)

    date_query: fecha de la query, para sacar el año
    typed_date: fecha pa timestampear
    """
    matchDate = re.match(r'(.*) a las (.*):(.*) hrs.', typed_date)
    if(matchDate):
        matchThisYear = re.match(r'(.*) de (.*)', matchDate.group(1))
        matchOtherYear = re.match(r'(.*)/(.*)/(.*)', matchDate.group(1))
        if(matchThisYear):
            d = int(matchThisYear.group(1)) if len(matchThisYear.group(1)) <= 2 else int(matchThisYear.group(1).split(' ')[1])
            m = mes[matchThisYear.group(2)]
            y = date_query.year
            date_timestamp = dt.datetime(
                year= y,
                month= m,
                day= d,
                hour=int(matchDate.group(2)),
                minute=int(matchDate.group(3))
                )
            return date_timestamp.timestamp()
        elif(matchOtherYear):
            d = int(matchOtherYear.group(1))
            m = int(matchOtherYear.group(2))
            y = int(matchOtherYear.group(3))
            y = y + 2000 if y < 50 else y + 1900
            date_timestamp = dt.datetime(
                year= y,
                month= m,
                day= d,
                hour=int(matchDate.group(2)),
                minute=int(matchDate.group(3))
                )
            return date_timestamp.timestamp()
        else:
            return 'NO_MATCHED_FORMAT'
    else:
        return 'NO_MATCHED_FORMAT'   


def str_2_timestamp(date_query: str, typed_date: str):
    """
    entrega el timestamp (float) del post o comentario
    date_query: el str del timestamp de consulta
    typed_date: la fecha que tiene escrita el postt en uwursos
    """
    matchDate = re.match(r'(.*), .*', typed_date)
    date_query_dt = dt.datetime.fromtimestamp(float(date_query))
    if(matchDate):
        return relative_date_2_timestamp(date_query_dt, typed_date)
    else:
        return date_2_timestamp(date_query_dt, typed_date)


def files_with_timestamp(file, type):
    """
    recibe un archivo sobre el cual leer y escribe la copia con el timestamp como float
    actualmente no escribe los headers en los archivos, hay que descomentarlalinea nomass
    
    file: nombre de un archivo tsv sobre el cual leer
    type: si es ROOT o CHILD para diferenciar headers
    """
    with open(file, 'r', encoding="utf-8") as src_file,\
        open(f'ts_{file}', 'w', encoding="utf-8", newline="") as out_file:

        if(type=="ROOT"):
            fields = ['id', 'titulo', 'autor', 'fecha_ts', 'tema', 'mensaje']
            fields_in= ['id', 'titulo', 'autor', 'fecha', 'tema', 'mensaje', 'current_time']
        elif(type=="CHILD"):
            fields = ['id', 'id_th', 'id_p', 'autor', 'fecha_ts', 'mensaje']
            fields_in = ['id', 'id_th', 'id_p', 'autor', 'fecha', 'mensaje', 'current_time']
        
        reader = csv.DictReader(
            src_file,
            fieldnames=fields_in,
            delimiter='\t'
                )
        writer = csv.DictWriter(
            out_file,
            fieldnames=fields,
            delimiter='\t'
            )
        
        # writer.writeheader()

        out_array = []
        for row in reader:
            temp = {}
            for field in fields:
                if field != 'fecha_ts':
                    temp[field] = row[field]
            ts = str_2_timestamp(row['current_time'], row['fecha'])
            temp['fecha_ts'] = ts
            out_array.append(temp)

        print(f"Escribiendo en el archivo ts_{file}")
        for line in out_array:
            writer.writerow(line)
        

if __name__ == '__main__':
    for file in os.listdir('.'):
        if file.endswith(".tsv"):
            if file.startswith("root_"):
                files_with_timestamp(file, "ROOT")
            elif file.startswith("child_"):
                files_with_timestamp(file, "CHILD")
        

tiempos_jejemplo = [
    "Hoy, hace 5 mins", 
    "Hoy, hace 13 segs",
    "Hoy, a las 00:02 hrs.",
    "Ayer, a las 01:13 hrs.",
    "Martes 7 de Mayo a las 20:43 hrs.",
    "2 de Mayo a las 13:49 hrs.",
    "16/12/05 a las 14:26 hrs."
    ]