# Proyecto de Gestión de Tareas

Este proyecto es una aplicación de gestión de tareas desarrollada con JavaFX. Permite a los usuarios agregar, editar, eliminar y visualizar tareas. La aplicación utiliza una base de datos para almacenar información sobre las tareas y los usuarios.

## Estructura del Proyecto

El proyecto se organiza en los siguientes paquetes y archivos:

- **com.uns.controller**: Contiene los controladores para la interfaz de usuario.
    - `ControllerViewMain.java`: Controlador para la vista principal.
    - `ControllerViewTareas.java`: Controlador para la vista de tareas.
- **com.uns.model**: Contiene las clases de modelo y acceso a datos (DAO).
    - `AbstractDAO.java`: Clase abstracta que proporciona métodos comunes para los DAOs.
    - `ActionsTask.java`: Interfaz que define los métodos para las operaciones CRUD de las tareas.
    - `ActionsUsers.java`: Interfaz que define los métodos para las operaciones CRUD de los usuarios.
    - `Conexion.java`: Clase que gestiona la conexión a la base de datos.
    - `Tarea.java`: Clase que representa una tarea.
    - `TareaDAO.java`: Clase que maneja las operaciones de acceso a datos para las tareas.
    - `Usuario.java`: Clase que representa un usuario.
    - `UsuariosDAO.java`: Clase que maneja las operaciones de acceso a datos para los usuarios.
- **res**: Contiene los recursos estáticos como CSS e imágenes.
    - **css**: Contiene los archivos de estilos CSS.
        - `home.css`: Archivo CSS para la vista home.
        - `main.css`: Archivo CSS para la vista principal.
    - **img**: Contiene las imágenes utilizadas en la aplicación.
        - `task.png`: Imagen de una tarea.
        - `to-do-list.png`: Imagen de una lista de tareas.
- **view**: Contiene los archivos FXML para la interfaz de usuario.
    - `view_home.fxml`: Archivo FXML para la vista home.
    - `view_main.fxml`: Archivo FXML para la vista principal.
- **META-INF**: Contiene archivos de configuración y metadatos.
- **Main**: Clase principal para iniciar la aplicación.

## Funcionalidades

### Gestión de Tareas

- **Agregar Tarea**: Permite al usuario agregar una nueva tarea con un título, descripción, fecha de creación, fecha de vencimiento, fecha de finalización, creador, responsable, estado de completado, prioridad y etiquetas.
- **Editar Tarea**: Permite al usuario editar una tarea existente.
- **Eliminar Tarea**: Permite al usuario eliminar una tarea seleccionada.
- **Visualizar Tareas**: Muestra una tabla con todas las tareas almacenadas en la base de datos.

### Gestión de Usuarios

- **Agregar Usuario**: Permite agregar nuevos usuarios.
- **Obtener Usuario por ID**: Permite obtener información de un usuario por su ID.
- **Obtener Todos los Usuarios**: Permite obtener una lista de todos los usuarios.
- **Actualizar Usuario**: Permite actualizar la información de un usuario existente.
- **Eliminar Usuario**: Permite eliminar un usuario por su ID.

## Uso de la Programación Orientada a Objetos (POO)

Este proyecto hace un uso de los conceptos de POO:

- **Abstracción**:
    - `AbstractDAO.java`: Proporciona una abstracción de las operaciones comunes de los DAOs, como la gestión de conexiones y el cierre de recursos.
- **Encapsulamiento**:
    - Clases como `Tarea.java` y `Usuario.java` encapsulan los datos y proporcionan métodos para acceder y modificar esos datos.
- **Herencia**:
    - `TareaDAO.java` y `UsuariosDAO.java` heredan de `AbstractDAO.java`, reutilizando métodos comunes y asegurando una gestión consistente de las conexiones a la base de datos.
- **Polimorfismo**:
    - Las interfaces `ActionsTask.java` y `ActionsUsers.java` permiten que diferentes implementaciones de DAOs puedan ser utilizadas de manera intercambiable.
- **Principio de Responsabilidad Única (SRP)**:
    - Cada clase tiene una única responsabilidad, por ejemplo, `TareaDAO.java` se encarga exclusivamente de las operaciones de acceso a datos para las tareas, mientras que `UsuariosDAO.java` maneja las operaciones para los usuarios.

## Cómo Ejecutar el Proyecto

1. **Configurar la Base de Datos**: Asegúrate de tener una base de datos configurada y actualiza los detalles de conexión en `Conexion.java`.
2. **Compilar y Ejecutar**: Compila el proyecto y ejecuta la aplicación desde tu IDE.
3. **Interfaz de Usuario**: Usa la interfaz de usuario para gestionar tareas y usuarios.

## Configuración de la Base de Datos

Este proyecto utiliza SQLite como sistema de gestión de base de datos. Se detallan los pasos para configurar la base de datos:

1. **Ubicación de la Base de Datos**: La base de datos SQLite se encuentra en la carpeta `db` con el nombre `base.db`.
2. **Estructura de la Base de Datos**:
    - Tabla `Tarea`:
      ```sql
      CREATE TABLE Tarea (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          titulo TEXT NOT NULL,
          descripcion TEXT,
          fechaCreacion DATETIME NOT NULL,
          fechaVencimiento DATETIME NOT NULL,
          fechaFinalizacion DATETIME,
          usuarioCreador INTEGER NOT NULL,
          usuarioResponsable INTEGER,
          completada BOOLEAN NOT NULL DEFAULT 0,
          prioridad TEXT CHECK(prioridad IN ('BAJA', 'MEDIA', 'ALTA')) NOT NULL DEFAULT 'MEDIA',
          etiquetas TEXT
      );
      ```
    - Tabla `Usuarios`:
      ```sql
      CREATE TABLE usuarios (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          nombre TEXT NOT NULL,
          apellido TEXT NOT NULL,
          correo TEXT NOT NULL UNIQUE,
          password TEXT NOT NULL,
          user TEXT NOT NULL UNIQUE
      );
      ```

3. **Creación de la Base de Datos**:
    - Asegúrate de tener SQLite instalado en tu sistema.
    - Abre una terminal y navega a la carpeta `db` del proyecto.
    - Ejecuta los siguientes comandos para crear la base de datos y las tablas:
      ```sh
      sqlite3 db/base.db
      ```
    - En el prompt de SQLite, ejecuta los comandos SQL proporcionados anteriormente para crear las tablas `Tarea` y `Usuarios`.

## Requisitos

- **Java**: JDK 8 .
- **JavaFX**: JavaFX SDK.
- **Base de Datos**: SQLite.

## Autor

Este proyecto fue desarrollado por Jesus Torres y Michelle Chavez.
