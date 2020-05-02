# Invoice Manager

Application written to manage invoices.

## Installation

To get your version clone your git repository and add file app.properties

```bash
https://github.com/KamilaNowak/invoice-manager.git
```
```bash.
├── /src/         
│     └── /main/    
│           ├── /main/     
│           └── /resources/   
│                    └── app.properties  
```
app.properties
```bash
sender = YOUR_EMAIL
pass = EMAIL_APP_PASSWORD

db.url = jdbc:postgresql://host/database_name
db.username = usernamame
db.password = password
db.driver = org.postgresql.Driver

cloud.aws.credentials.accessKey = ACCESS_KEY
cloud.aws.credentials.secretKey = SECRET_KEY
aws.bucket.name = AWS_BUCKET_NAME
aws.region.name = AWS_REGION
```
## Built With
- Kotlin 1.2
- TornadoFX 1.7
- Java 8
- W3C CSS
- PostgreSQL 11 
- HikariCP 3.4
- JUnit Jupiter 5
- AWS S3
- iText 7

## Application snippets
### Register and login
![](https://github.com/KamilaNowak/invoice-manager/blob/master/snippets/snippet_register.gif)

### Main dashboard view with all invoices and statistics
![](https://github.com/KamilaNowak/invoice-manager/blob/master/snippets/snippet_dashboard.gif)

### Adding invoice
![](https://github.com/KamilaNowak/invoice-manager/blob/master/snippets/snipeet_add.gif)

### Viewing generated PDF files ans sending notification mail
![](https://github.com/KamilaNowak/invoice-manager/blob/master/snippets/snippet_pdf.gif)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## Authors

* **Kamila Nowak**  - [KamilaNowak](https://github.com/KamilaNowak)

## License
This project is licensed under the MIT License
