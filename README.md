# ProyectoInmobiliaria
Trabajo Final Dispositivos Móviles

## Descripción de la Aplicación

**Propósito:**
"ProyectoInmobiliaria" es una aplicación móvil diseñada para la gestión de propiedades inmobiliarias. Facilita la administración de perfiles de usuarios, inmuebles, inquilinos y contratos asociados, proporcionando una interfaz organizada para las operaciones inmobiliarias.

**Estructura de la Interfaz de Usuario (UI):**

1.  **Pantalla de Inicio de Sesión:**
    *   La aplicación cuenta con una pantalla de inicio de sesión inicial que requiere un "Usuario" y una "Contraseña".
    *   Se incluye un botón "Iniciar sesión" y una opción "¿Olvidó su contraseña?".
    *   Un "Logo inmobiliaria" se muestra para branding.

2.  **Pantalla Principal con DrawerActivity:**
    *   Después de iniciar sesión, el usuario accede a la pantalla principal, que implementa un `DrawerLayout` para la navegación.
    *   **Barra de Aplicación (App Bar):** Incluye una barra superior que contendrá el título de la sección actual.
    *   **Contenedor de Fragments (`fragment_container`):** El área principal de contenido de la aplicación, donde se cargan dinámicamente diferentes módulos (fragments) seleccionados desde el menu de navegación.
    *   **Navigation Drawer:** Deslizable desde el borde inicial de la pantalla, ofrece los siguientes elementos de menú para acceder a las diferentes secciones de la aplicación:
        *   **Inicio:** (Aunque no hay un `HomeFragment` explícito, es un elemento común para la pantalla principal).
        *   **Perfil:** Gestión de la información del perfil del usuario.
        *   **Inmuebles:** Gestión y visualización de propiedades.
        *   **Inquilinos:** Visualizacion de la información de los inquilinos.
        *   **Contratos:** Visualización de contratos.
        *   **Logout:** Para cerrar la sesión de la aplicación.
    *   **Cabecera del Menu de Navegación (`nav_header_drawer`):** Contiene un área designada para información adicional, se personaliza con la información del usuario logueado.

**Funcionalidades:**

*   **Autenticación de Usuario:** Permite a los usuarios iniciar sesión de forma segura.
*   **Gestión de Perfil:** Posibilita a los usuarios ver y editar su información de perfil.
*   **Gestión de Inmuebles:** Funcionalidad para administrar propiedades, incluyendo la visualización y edición.
*   **Gestión de Inquilinos:** Permite la visualización de los datos de los inquilinos en base a los contratos vigentes.
*   **Gestión de Contratos:** Permite la visualización de los contratos asociados a las propiedades y los inquilinos. Tambien puede ver los pagos que el inquilino realizo.
*   **Cierre de Sesión:** Proporciona una forma segura de cerrar la sesión de la aplicación.

**Modelos de Datos:**

*   `Contrato.java`: Representa el modelo de datos para los contratos.
*   `Inquilino.java`: Representa el modelo de datos para los inquilinos.
*   `Inmueble.java`: Representa el modelo de datos para los inmuebles.
*   `Pago.java`: Representa el modelo de datos para los pagos de los contratos vigentes.
*   `Propietario.java`: Representa el modelo de datos para el propietario.

**Requerimientos Solicitados:**

* Login/logout de propietarios
* Ver y editar su perfil
* Listar sus inmuebles
* Habilitar/Deshabilitar un inmueble de este propietario
* Agregar un nuevo inmueble con foto y por defecto deshabilitado
* Listar contratos por Inmuebles y sus pagos
* NOTA: La aplicación no debe enviar el id del propietario. Este debe ser recuperado a través del token.
  Todas las funcionalidades, salvo el Login, deben requerir estar autenticado.

**Objetivos:**

* Todos los objetivos se pueden considerar cumplidos. En cuanto a la NOTA, la aplicación utiliza correctamente un token de Authorization para la mayoría de las llamadas a la API, lo que implica que el ID del propietario se recupera en el lado del servidor a través del token, en lugar de ser enviado explícitamente desde el cliente. No se observa ningún idPropietario u ownerId enviado directamente en los cuerpos de las solicitudes (@Body o @Part).
* Autenticación requerida para la mayoría de las funcionalidades: Todas las funcionalidades, excepto el método loginForm, requieren explícitamente un encabezado Authorization. Esto satisface el requisito de que todas las funcionalidades (excepto el inicio de sesión) requieren autenticación.

  Este proyecto es una robusta aplicación de gestión inmobiliaria con una navegación clara y modular, diseñada para gestionar eficientemente los aspectos clave de las propiedades, inquilinos y contratos.