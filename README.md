# **WEB SERVER**

## Premesse

È possibile (e consigliato) leggere questo file tramite il web server stesso andando al path `/readme`, in questo caso, in fondo si trova anche il pulsante per tornare alla home page.

## **Il Progetto**

Questo progetto è stato realizzato da Dalla Betta Davide nel corso del mese di novembre e dicembre. Lo scopo del progetto è quello di implementare, tramite l'utilizzo della classe `Socket` di java, il protocollo HTTP per l'interpretazione di richieste HTTP e l'invio di risposte dello stesso protocollo. L'obiettivo finale è quello di creare un server contenente un semplice sito web che permetta la navigazione.

### **Versione 1.0**

#### **Obiettivi 1.0**

La prima versione deve essere in grado di leggere le richieste dal client (il browser web) e inviare la pagina HTML a lui interessata. Inizialmente le pagine sono solo 3 e sono state definite in delle costanti stringa `INDEX`, `PROVA` e `ERRORE`; all'arrivo della richiesta viene letto l'URI all'interno e viene inviata la prima se esso ha valore `"/"`, la seconda se questo ha valore `"/prova"` e l'ultima in ogni altro caso (in questo caso la risposta è inviata con codice 404).

#### **Implementazione 1.0**

Per la realizzazione di questi obiettivi la classe principale che è stata creata è `WebServer`. Essa viene istanziata con il passaggio come parametro di un numero di porta e si mette in ascolto infinito per le richieste dei client e, in base a esse, invia le pagine come spiegato in precedenza.

#### **Aggiunte 1.0**

Per semplificare l'implementazione e aumentare la leggibilità sono state create alcune classi complementari: HttpRequest permette il parsing della richiesta del client in un oggetto più strutturato che permette di accedere ai vari campi con maggiore facilità e HttpResponse che permette di creare una risposta HTTP come da protocollo e dividerla per mandarla a segmenti di minore dimensione. Inoltre per rappresentare la connessione con il Client è stata realizzata una classe interna al WebServer chiamata `ClientHandler` che si occupa di tutte le cose riguardanti il singolo Client.

### **Versione 2.0**

#### **Obiettivi 2.0**

Migliorare la versione precedente per far sì che permetta la connessione con diversi client su Thread diversi.

#### **Implementazione 2.0**

È stato sufficiente modificare la classe `ClientHandler` facendo in modo che estenda la classe java `Thread` e modificare per far sì che il codice fosse all'interno del metodo `run()`.

### **Versione 3.0**

#### **Obiettivi 3.0**

Utilizzare un sistema che permetta al WebServer di processare proceduralmente le richieste e che quindi non abbia bisogno di modifiche al codice in caso di aggiunta di una pagina. Deve avere una variabile d'istanza `documentRoot` che specifichi la posizione delle risorse. Deve risultare possibile spedire file HTML e immagini.

#### **Implementazione 3.0**

Per implementare ciò è stato sufficiente modificare il costruttore del WebServer per passargli anche la documentRoot e modificare il metodo `HttpResponse handleRequest(HttpRequest)` per fargli prendere il file dal documentRoot. Leggendo l'estensione del file richiesto si capisce anche il tipo di contenuto e viene anch'esso stpedito nell'header `Content-Type`

#### **Aggiunte 3.0**

In più sono implementati anche i `Content-Type` `"text/css"` e `"text/js"` per l'uso di fogli di stile e script e sono state create semplici pagine anche con l'utilizzo di essi.

### **Versione 4.0**

#### **Obiettivi 4.0**

Per completare il progetto sono state infine aggiunte alcune feature per permettere di utilizzare lo stesso webserver senza dover cambiare una singola riga di codice. Di seguito le principali:

- Creazione di un sistema per inviare risposte sulla base delle richieste (senza guardare direttamente nella directory) e quindi la possibilità di disabilitare la funzione di "autoFetch".
- Aggiunta di una classe `HTMLElement` che permette la creazione di stringhe HTML all'interno di java (sperimentale).
- Implementazione del parsing di un file markdown (come questo) per la visualizzazione in HTML dello stesso.

#### **Implementazione 4.0**

- Per fare in modo di controllare singolarmente le richieste, è stata creata una classe `RequestHandler` contenente un'interfaccia `ResponseSender` che, anche tramite l'utilizzo di una lamba expression, permette di creare un metodo che ritorni una `HttpResponse` e così facendo, eseguendo una ricerca lineare, è possibile inviare la risposta corretta alla richiesta corretta. Per aggiungere un Handler è sufficiente aggiungere al main una chiamata al metodo `addRequestHandler(Method, Uri, ResponseSender)`.
- Per quanto riguarda la classe `HTMLElement`, è stato sufficiente creare una struttura dati contenente il tag, il contenuto e una lista di attributi "chiave-valore".
- Per implementare il parsing è stata importata tramite maven la libreria di commonmark per il parsing in HTML. Il dettaglio della libreria si trova al seguente [link](https://github.com/commonmark/commonmark-java).

[HOME PAGE](..)
