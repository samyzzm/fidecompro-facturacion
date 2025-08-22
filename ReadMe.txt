# Fidecompro - Sistema de Facturación

Este es un sistema de facturación desarrollado en Java para la cadena Fidecompro, como parte del segundo avance del curso de Programación Cliente-Servidor. El sistema incluye manejo de clientes, productos, generación de facturas y validación de stock, con una interfaz gráfica construida en Swing.

---

##  Características

- Inicio de sesión con validación de credenciales
- Registro de clientes y productos
- Generación de facturas para clientes
- Verificación de stock disponible
- GUI simple e intuitiva usando Java Swing
- Manejo de excepciones personalizadas (`StockInsuficienteException`)
- Uso de herencia, polimorfismo y colecciones
- Proyecto versionado con Git y GitHub

---

##  Clases del proyecto

- `FidecomproApp.java`: Punto de entrada y control de la GUI
- `Cliente.java`: Representa un cliente (nombre y teléfono)
- `Producto.java`: Representa un producto y su stock; lanza excepciones si no hay suficientes unidades
- `Usuario.java`: Clase abstracta base para usuarios
- `UsuarioAdmin.java`: Subclase que representa a un usuario administrador
- `UsuarioVendedor.java`: Subclase que representa a un usuario del área de ventas
- `StockInsuficienteException.java`: Excepción personalizada para manejar errores de stock

---

##  Requisitos

- Java 8 o superior
- IDE como IntelliJ IDEA, VS Code o NetBeans
- Git (opcional, para clonar el proyecto)

---

```bash
git clone https://github.com/samyzzm/fidecompro-facturacion.git
