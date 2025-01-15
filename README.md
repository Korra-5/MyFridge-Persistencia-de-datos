## MyFridge

### Aplicación Funcional MyFridge

**MyFridge** es una aplicación diseñada para ayudarte a gestionar el contenido de tu frigorífico de manera eficiente y organizada. Su funcionamiento es similar al de un blog de notas, permitiéndote registrar y controlar los diferentes productos que tienes, junto con su cantidad y descripciones adicionales.

#### Características Principales:
- **Registro de productos:** Anota los productos disponibles en tu frigorífico.
- **Gestor de cantidades:** Mantén un control claro de la cantidad de cada producto.
- **Notas personalizadas:** Agrega descripciones o detalles adicionales a cada producto.

Esta aplicación está diseñada para hacer más fácil y rápido el control de tu inventario doméstico, ayudándote a evitar desperdicios y planificar tus compras con mayor precisión.

---

### Persistencia de Datos con DataStore

La elección de **DataStore** como sistema de persistencia de datos en MyFridge se basa en los siguientes motivos:

1. **Escalabilidad:**
   - **DataStore** es ideal para proyectos de gran escala gracias a su diseño moderno y eficiente.

2. **Eficiencia:**
   - A diferencia de **SharedPreferences**, **DataStore** es más eficiente al manejar grandes volúmenes de datos o realizar operaciones frecuentes.

3. **Manejo de estructuras complejas:**
   - El uso de **Proto DataStore** permite gestionar objetos de tipo "Producto" con mayor control y flexibilidad, lo cual es esencial para una aplicación como MyFridge.

4. **Diseño moderno:**
   - **DataStore** está basado en **Coroutines** y **Flow**, lo que garantiza un acceso asíncrono y seguro a los datos, evitando bloqueos en el hilo principal.

En resumen, **DataStore** ofrece una solución robusta y moderna para la persistencia de datos, alineada con las necesidades de una aplicación escalable y orientada a la gestión eficiente de productos.
