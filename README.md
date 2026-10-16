# Salvador de Semestres 

Una aplicación móvil para Android creada para ayudar a los estudiantes a organizar sus tiempos de estudio. Funciona como un temporizador inteligente que además guarda un registro de todo lo que has estudiado.

##  ¿Qué hace la aplicación?

*   **Crear cuenta y Login:** Puedes registrarte y tu cuenta quedará guardada en tu teléfono. La app recordará tu sesión abierta para que no tengas que entrar tu clave cada vez.
*   **Estudiar:** Tienes un cronómetro donde puedes elegir cuánto tiempo quieres estudiar (15, 25 o 45 minutos). Puedes pausarlo el tiempo o detenerlo en cualquier momento.
*   **Descripcion de estudio:** Al terminar de estudiar, la app te pedirá que escribas lo que aprendiste con la seccion de estudio.
*   **Ver tu Historial:** Todas tus sesiones terminadas (con su tema, tiempo y descripción) se guardarán en una tabla para que puedas revisar lo que haz estudiado.
*   **Tu Perfil:** Una pantalla donde puedes ver cuántos minutos y sesiones llevas en total. También puedes cambiar tu contraseña o borrar tu cuenta para siempre.
*   **Mensajes de apoyo:** Si te estresas y agitas el teléfono mientras estudias, la app detectará el movimiento y te mandará un mensaje motivacional.

## Herramientas utilizadas 

*   **Diseño (XML):** Uso de `ConstraintLayout` para que las pantallas se adapten bien al celular, y uso de menús desplegables (`Spinner`).
*   **Base de Datos (SQLite):** Se guardan, buscan, actualizan y borran (CRUD) los usuarios de forma local en el teléfono.
*   **SharedPreferences:** Para mantener la sesión del usuario iniciada.
*   **Intents:** Para viajar de una pantalla a otra y llevar información de la sesión.
*   **Sensor de Hardware:** Uso del acelerómetro del celular.
*   **Hilos:** Para buscar a los usuarios en la base de datos sin que la pantalla se quede pegada mientras busca.