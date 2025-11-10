import oracledb
import os
import sys
import time
import random
from dotenv import load_dotenv

load_dotenv() # Carga las variables del archivo .env

try:
    USER = os.environ.get("DB_USER")
    PASSWORD = os.environ.get("DB_PASS")
    DSN = os.environ.get("DB_DSN")
    WALLET_PATH = os.environ.get("WALLET_PATH")
    
    if not all([USER, PASSWORD, DSN, WALLET_PATH]):
        print("*ERROR*: Faltan variables de entorno.")
        print("Asegúrate de que el archivo .env exista y contenga DB_USER, DB_PASS, DB_DSN, y WALLET_PATH.")
        sys.exit(1)
except KeyError:
    print("*ERROR*: Variables de entorno no configuradas.")
    sys.exit(1)

try:
    print("--- INICIANDO SIMULADOR DE MÁQUINA ---")
    
    with oracledb.connect(user=USER, password=PASSWORD, dsn=DSN,
                       config_dir=WALLET_PATH,
                       wallet_location=WALLET_PATH, 
                       wallet_password=PASSWORD) as connection:
        
        print(f"**CONECTADO** como: {USER}. Buscando muestras 'Recibida'...")

        sql_select_recibida = "SELECT MU_ID FROM MUESTRA WHERE MU_ESTADO = 'Recibida'"

        print("\n --- RECIBIENDO NUEVAS MUESTRAS... ---")
        sql_update_estado = "UPDATE MUESTRA SET MU_ESTADO = :nuevo_estado WHERE MU_ID = :mu_id"
        
        with connection.cursor() as cursor:
            cursor.execute(sql_select_recibida)
            filas = cursor.fetchall() 
            
            if not filas:
                print("\nNo se encontraron muestras 'Recibida'.")
            else:
                print(f"Se encontraron {len(filas)} muestras para iniciar procesamiento.")
                for fila in filas:
                    mu_id = fila[0]
                    cursor.execute(sql_update_estado, nuevo_estado='En Proceso', mu_id=mu_id)
                    print(f"Muestra {mu_id} actualizada a 'En Proceso'.", flush=True)
                    time.sleep(0.5)

            connection.commit()
            print("\n--- MUESTRAS NUEVAS RECIBIDAS EXITOSAMENTE ---")

        sql_select_en_proceso = "SELECT MU_ID, MU_TIPO FROM MUESTRA WHERE MU_ESTADO = 'En Proceso'"
        sql_insert_resultado = "INSERT INTO RESULTADO (resultado_id, mu_id, nombre_prueba, valor_numerico, unidad) " \
                                "VALUES (resultado_id_seq.NEXTVAL, :mu_id, :nombre_prueba, :valor_numerico, :unidad)"
        
        print("\n--- GENERANDO RESULTADOS Y ACTUALIZANDO ESTADO DE MUESTRAS ---")
        with connection.cursor() as cursor:
            cursor.execute(sql_select_en_proceso)
            filas = cursor.fetchall()

            MAPEO_PRUEBAS = {
                "Sangre": ("Glucosa", 70.0, 140.0, "mg/dL"),
                "Orina": ("pH", 4.5, 8.0, "pH"),
                "Heces": ("Grasa Fecal", 2.0, 7.0, "g/dL"),
                "Tejidos": ("Densidad Celular", 100.0, 500.0, "cél/μL"),
                "LCR": ("Proteína LCR", 15.0, 60.0, "mg/dL"),
                "Saliva": ("Cortisol Salival", 100.0, 750.0, "ng/dL"),
                "Exudados y Secreciones": ("Recuento Leucocitos", 5000.0, 20000.0, "cél/μL"),
                "Pelo/Uñas": ("Nivel Arsénico", 0.1, 1.0, "µg/g"),
                "Células": ("Recuento Celular", 100.0, 1000.0, "cél/campo"),
                "Gases": ("pCO2 (Gasometría)", 35.0, 45.0, "mmHg")
            }

            if not filas:
                print("No se encontraron muestras 'En Proceso' para finalizar...")
            else:
                print(f"Se encontraron {len(filas)} muestras para finalizar, generando resultados...")
                for fila in filas:
                    mu_id = fila[0]
                    mu_tipo = fila[1]

                    config_prueba = MAPEO_PRUEBAS.get(mu_tipo, ("Generic Test", 1.0, 10.0, "N/A"))

                    nombre_prueba_sim = config_prueba[0]
                    valor_min_sim = config_prueba[1]
                    valor_max_sim = config_prueba[2]
                    unidad_sim = config_prueba[3]

                    valor_simulado = round(random.uniform(valor_min_sim, valor_max_sim), 2)

                    cursor.execute(sql_insert_resultado, {
                        "mu_id":mu_id,
                        "nombre_prueba":nombre_prueba_sim,
                        "valor_numerico":valor_simulado,
                        "unidad":unidad_sim
                    })
                    print(f"    > Muestra {mu_id}({mu_tipo}). Resultado generado: {valor_simulado} {unidad_sim}")

                    time.sleep(1)
                    cursor.execute(sql_update_estado, {
                        "nuevo_estado": "Completada",
                        "mu_id": mu_id
                    })
        connection.commit()
        print("--- GENERACION DE RESULTADOS COMPLETADA ---")


except oracledb.Error as error:
    print(f"\nERROR CRÍTICO DE CONEXIÓN O SQL (Usuario: {USER}):")
    print(error)
    sys.exit(1)
