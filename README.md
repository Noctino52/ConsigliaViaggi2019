# ConsigliaViaggi2019

Progetto sviluppato per l'esame “Ingegneria del software” del Corso di Laurea in Informatica presso l'Università degli studi di Napoli Federico II.
# Breve descrizione

Si tratta di una piattaforma di supporto relativa a viaggi: L’applicazione mobile consente agli utenti di poter effettuare ricerche di strutture ricettive e di visualizzare le singole nei dettagli, includendo eventuali recensioni e/o strutture ricettive ad essa correlate. La ricerca può essere effettuata in diversi modi: mediante parole chiave, per tipo della struttura ricettiva (Hotel, Ristorante, Attrazione turistica) oppure geolocalizzata (ricerca su mappa tramite API Google e individuazione tramite GPS). Inoltre i risultati di ricerca potranno essere filtrati e/o ordinati così come le recensioni di ogni struttura.
# Panoramica delle tecnologie

Come DBMS è stato utilizzato MySQL, questo viene interrogato da un server scritto in Spring Boot ed espone vari metodi all'esterno. Sia Client che Server sono scritti in Kotlin. Per risolvere vari problemi di dipendenze sono stati utilizzati Maven e Grandle, quindi un pom.xml per il server, e un build.grandle per il mobile.

Per garantire una scalabilità del software sono stati utilizzati i seguenti servizi: AWS Elastic Beanstalk: Utilizzato per hostare il server Spring (https://aws.amazon.com/it/elasticbeanstalk/) AWS RDS: Utilizzato per hostare il database scritto in MySql (https://aws.amazon.com/it/rds/)

L'applicativo fa utilizzo del framework di autenticazione, autorizzazione e sicurezza Spring Security con token jwt. Per criptare le password nel database è stato utilizzato l'algoritmo BCrypt.
# Note

Il server spring è attualmente hostato su Elastic Beanstalk, questo vuol dire che per testare l'app mobile si può scaricare anche solo quest'ultima. Anche il DB MySql è attualmente hostato in RDS, questo vuol dire che nel server spring in src/main/resorces il file databaseInformation.properties contiene le informazioni per accedere al suddetto DB, in caso qualcuno voglia collegarlo al proprio sà come fare.

NON E' CONSENTITO IL RIUSO ANCHE PARZIALE DI CODICE E DOCUMENTAZIONE PRESENTE IN QUESTA PAGINA.
