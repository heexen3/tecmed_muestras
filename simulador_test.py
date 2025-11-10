import oracledb
import os
import sys
from dotenv import load_dotenv # <--- IMPORTANTE: Importar la librería

load_dotenv() # Carga las variables del archivo .env

# --- 2. LEER VARIABLES (Modo Seguro) ---
# Lee las credenciales desde el entorno (nunca escritas en el código)
try:
    USER = os.environ.get("DB_USER")
    PASSWORD = os.environ.get("DB_PASS")
    DSN = os.environ.get("DB_DSN")
    WALLET_PATH = os.environ.get("WALLET_PATH")
    
    # Verificación de que las variables cargaron
    if not all([USER, PASSWORD, DSN, WALLET_PATH]):
        print("❌ ERROR: Faltan variables de entorno.")
        print("Asegúrate de que el archivo .env exista y contenga DB_USER, DB_PASS, DB_DSN, y WALLET_PATH.")
        sys.exit(1)

except KeyError:
    print("❌ ERROR: Variables de entorno no configuradas.")
    sys.exit(1)


# --- 3. LÓGICA DE NEGOCIO (Sin cambios, ahora es segura) ---
try:
    print("--- INICIANDO SIMULADOR DE MÁQUINA (Modo Seguro) ---")
    
    with oracledb.connect(user=USER, password=PASSWORD, dsn=DSN,
                       config_dir=WALLET_PATH,
                       wallet_location=WALLET_PATH, 
                       wallet_password=PASSWORD) as connection:
        
        print(f"✅ Conexión exitosa como: {USER}. Buscando muestras 'Recibida'...")

        sql_select = "SELECT MU_ID FROM MUESTRA WHERE MU_ESTADO = 'Recibida'"
        sql_update = "UPDATE MUESTRA SET MU_ESTADO = :nuevo_estado WHERE MU_ID = :id_a_actualizar"
        
        with connection.cursor() as cursor:
            
            cursor.execute(sql_select)
            filas = cursor.fetchall() 
            
            if not filas:
                print("\nNo se encontraron muestras 'Recibida'.")
            else:
                print(f"Se encontraron {len(filas)} muestras para actualizar.")
                for fila in filas:
                    mu_id = fila[0]
                    cursor.execute(sql_update, nuevo_estado='En Proceso', id_a_actualizar=mu_id)
                    print(f"Muestra {mu_id} actualizada a 'En Proceso'.")
            
            connection.commit()
            print("\n--- SIMULACIÓN COMPLETA ---")
        
except oracledb.Error as error:
    print(f"\n❌ ERROR CRÍTICO DE CONEXIÓN O SQL (Usuario: {USER}):")
    print(error)
    sys.exit(1)