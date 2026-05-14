# Especificación de Requerimientos del Sistema de Gestión de Parqueadero

## Información del Sistema

- **Universidad:** CORHUILA
- **Programa:** Ingeniería de Sistemas
- **Curso:** Análisis de Sistemas
- **Fecha:** 27 de septiembre de 2024

**Elaborado por:**
- Linda Sofía Moyano Silva
- Juan Pablo Borrero Morales
- José Andrés Cardona Rojas
- Carlos Mauricio Leal Medina
- Daniel Felipe Cerquera Idrobo
- Juan Nicolás Osorio Barrero

---

## Requerimientos del Sistema

### Requerimientos Funcionales

#### Gestión de Usuarios
1. **Inicio de sesión de usuarios registrados**: El sistema debe permitir que los usuarios inicien sesión utilizando su correo electrónico y contraseña.
2. **Gestión de perfil de usuario**: Los usuarios podrán ver y editar su perfil, que incluye datos como nombre, fecha de nacimiento, ubicación, correo electrónico y configuración de notificaciones.

#### Disponibilidad y Selección de Espacio
1. **Visualización de espacios disponibles por tipo de vehículo**: El sistema debe mostrar en tiempo real los espacios disponibles para cada tipo de vehículo (carro, moto y bus) en un mapa del parqueadero.
2. **Selección de espacio de parqueo**: Los usuarios podrán seleccionar un espacio específico para su vehículo según la disponibilidad.

#### Reservación y Confirmación de Reserva
1. **Reserva de espacios de parqueo**: Los usuarios podrán realizar una reservación anticipada de un espacio de parqueo, especificando la fecha y hora de ingreso y salida.
2. **Confirmación de reserva**: Después de realizar la reserva, el sistema mostrará una pantalla de confirmación con los detalles de la misma (tipo de vehículo, matrícula, color y marca del vehículo, fechas de ingreso y salida).

#### Ubicación y Navegación
1. **Ubicación del parqueadero en mapa**: El sistema debe integrar un mapa que muestre la ubicación exacta del parqueadero para ayudar a los usuarios a llegar fácilmente.

#### Gestión de Comentarios
1. **Comentarios de usuarios**: Los usuarios podrán enviar comentarios y sugerencias a través de una sección dedicada en la aplicación.

#### Configuración y Notificaciones
1. **Configuración de notificaciones**: Los usuarios pueden activar o desactivar notificaciones como recordatorios de reserva y actualizaciones de disponibilidad.
2. **Configuración de preferencias**: Los usuarios podrán configurar opciones generales como la activación de la ubicación y los permisos de notificaciones.

#### Gestión de Pagos
1. **Pago de tarifas de parqueo**: El sistema debe permitir a los usuarios realizar pagos con tarjeta de crédito, débito y pagos móviles.
2. **Historial de pagos**: Los usuarios podrán ver el historial de pagos realizados.

---

### Requerimientos No Funcionales

#### Seguridad
1. **Protección de datos personales y financieros**: El sistema debe cumplir con los estándares de seguridad para proteger los datos personales y financieros de los usuarios, incluyendo cifrado de datos sensibles.
2. **Control de acceso**: Solo usuarios autenticados deben tener acceso a funciones sensibles, como la edición de perfil y el historial de pagos.

#### Rendimiento
1. **Tiempo de respuesta**: Las operaciones críticas (como reserva, confirmación de espacio y pagos) deben ejecutarse en un tiempo inferior a 2 segundos en condiciones normales de carga.
2. **Disponibilidad**: El sistema debe estar disponible el 99.9% del tiempo para los usuarios.

#### Usabilidad
1. **Interfaz intuitiva**: La aplicación debe contar con una interfaz fácil de usar, que permita a los usuarios navegar y realizar reservas de manera intuitiva, especialmente en dispositivos móviles.
2. **Accesibilidad**: La aplicación debe cumplir con estándares de accesibilidad para personas con discapacidades.

#### Escalabilidad
1. **Capacidad para manejo de carga**: El sistema debe ser capaz de manejar un gran número de usuarios simultáneos, especialmente durante las horas pico.
2. **Adaptabilidad**: La arquitectura del sistema debe permitir agregar nuevos módulos o características en el futuro sin afectar la operación actual.

#### Mantenimiento
1. **Documentación técnica**: El sistema debe contar con una documentación detallada para facilitar futuras actualizaciones y mantenimiento.
2. **Actualización de versiones**: La arquitectura debe permitir implementar actualizaciones sin interrumpir el servicio.

---

## Mockup

### Secciones Principales

1. **Inicio de sesión:** Menú de usuario.
2. **Disponibilidad de espacios para carros:** Espacios ocupados y disponibles.
3. **Disponibilidad de espacios para motos:** Espacios ocupados y disponibles.
4. **Disponibilidad de espacios para buses:** Espacios ocupados y disponibles.
5. **Ubicación del parqueadero:** Ayuda a los clientes a llegar fácilmente.
6. **Comentarios:** Sección para recibir sugerencias y comentarios de los usuarios.
7. **Ajustes del programa:** Configuración personalizada para los usuarios, incluyendo la detección de ubicación y soporte al cliente.
8. **Configuración de perfil del usuario.**

### Funcionalidades del Mockup (Modelo MoSCoW)

1. **Menú para usuarios registrados.**
2. **Visualización de espacios para carros.**
3. **Visualización de espacios para motos.**
4. **Visualización de espacios para buses.**
5. **Ubicación del parqueadero.**
6. **Sección de comentarios de los usuarios.**
7. **Ajustes del sistema para comodidad del usuario.**
8. **Configuración del perfil de usuario.**

## Validación del Mockup con Usuarios

Se realizó una validación con la Sra. Marta, encargada de un parqueadero. El mockup fue útil y sencillo de usar, destacando la importancia de la digitalización y el ahorro de papel.

## Recolección de Datos

### Entrevistas
- **Objetivo:** Recoger información clave de los propietarios, empleados y usuarios.
  - **Desafíos en la administración:** Dificultad para saber cuántos vehículos ocupan espacios.
  - **Proceso de entrada/salida:** Complicado en horas pico.
  - **Métodos de pago:** Transferencias y efectivo.
  - **Disponibilidad de espacios:** Falta de control en la ocupación de espacios.
  - **Satisfacción de los usuarios:** Satisfechos en seguridad, pero falta un sistema de control de disponibilidad.

### Cuestionario
- **Objetivo:** Obtener respuestas cuantitativas y cualitativas de los usuarios.
  - Ejemplo de preguntas:
    - Frecuencia de uso del parqueadero.
    - Satisfacción con el sistema actual.
    - Preferencia por reserva anticipada.
    - Métodos de pago preferidos.

### Observación Directa
- **Objetivo:** Observar el comportamiento de los usuarios.
  - **Flujo de vehículos.**
  - **Tiempo de espera en entrada y salida.**
  - **Interacción con el sistema de pago.**

## Análisis con Design Thinking

1. **Empatizar:** Identificación de patrones y necesidades en las entrevistas y observaciones.
2. **Definir:** Problemas identificados: falta de visibilidad en la disponibilidad de espacios, tiempos de espera largos, y métodos de pago ineficientes.
3. **Idear:** Propuestas: Panel de disponibilidad de espacios, reservación anticipada, pagos digitales rápidos, y mejor seguridad.

## Link al Proyecto Figma
[Proyecto en Figma](https://www.figma.com/design/AXumRPkCXhDl77HCnHNtWM/Figma-project-(app-for-parking-lot)-(Community)?node-id=0-1&t=ieMRxDdxrL6GxMAW-1)
