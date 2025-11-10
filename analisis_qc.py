import pandas as pd
import oracledb
import os
import sys
from dotenv import load_dotenv

load_dotenv()


try:
    USER = os.environ.get("DB_USER")
    PASSWORD = os.environ.get("DB_PASS")
    DSN = os.environ.get("DB_DSN")
    WALLET_PATH = os.environ.get("WALLET_PATH")

    WALLET_PASSWORD = PASSWORD

    if not all([USER, PASSWORD, DSN, WALLET_PATH]):
        raise Exception("Faltan variables de entorno para la conexion.")
except Exception as e:
    print(f"ERROR DE CONFIGURACION: {e}")
    sys.exit(1)

def analizar_resultados():
    try:
        print("--- INICIANDO ANALISIS DE CALIDAD CON PANDAS ---")

        with oracledb.connect(
            user=USER,
            password=PASSWORD,
            dsn=DSN,
            config_dir=WALLET_PATH,
            wallet_location=WALLET_PATH,
            wallet_password=WALLET_PASSWORD
        ) as connection:
            
            print("CONEXION EXITOSA. Cargando datos...")

            sql_query = "SELECT * FROM RESULTADO"

            # DataFrame
            df = pd.read_sql_query(sql_query, connection)
            df['VALOR_NUMERICO'] = pd.to_numeric(df['VALOR_NUMERICO'])

            media = df['VALOR_NUMERICO'].mean()
            std_dev = df['VALOR_NUMERICO'].std()

            upper_lim = media + std_dev*3
            lower_lim = media - std_dev*3

            condicion_outliers = (
                (df["VALOR_NUMERICO"] > upper_lim) 
                | 
                (df["VALOR_NUMERICO"] < lower_lim)
                )
            
            outliers = df[condicion_outliers]
            # 'outliers' toma el DF y devuelve solo las filas donde se cumple la condicion
            #                  --- Regla de las 3-STD ---
            #       outlier => cualquier valor que se encuentra a mas de 3 STD
            # si es True, guarda el subconjunto en la variable 'outliers'

            # 4. Reporte final
            print("-" * 50)
            print(f"Analisis Estadistico (Media: {media:.2f}, STD: {std_dev:.2f})")
            print("-" * 50)

            if outliers.empty:
                print("CONTROL DE CALIDAD EXITOSO! No se detectaron anomalias.")
            else:
                print(f"ALERTA! Se detectaron {len(outliers)} RESULTADOS ANOMALOS (outliers):")
                print(50*"=")
                print(outliers[['RESULTADO_ID', 'VALOR_NUMERICO', 'NOMBRE_PRUEBA']]) 
                print(50*"=")
            print("-" * 50)
    except oracledb.Error as error:
        print(f"ERROR SQL/ORACLE. {error}")
    except Exception as e:
        print(f"ERROR INESPERADO en el analisis: {e}")

if __name__ == "__main__":
    analizar_resultados()



            









