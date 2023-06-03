# spring-cds-service-template

## Getting started:

1. Start "CDSServiceTemplateApplication.java"
2. Open Terminal:
```bash
cd ui
npm start
```

3. Start CDS Hooks Test with the main class in "CDSHooks.java"


## Beispiel REST Schnittstelle im Projekt:

Die Schnittstelle lässt sich über die URL http://localhost:8080/cds-services/trials aufrufen und man stellt die Anfragetype auf POST ein.

Pflichtinhalte für die Anfrage zur Standard REST Schnittstelle:  
- Content-Type: application/json
- Body Inhalt: cds-hook-example-body.json

Einfach einen Breakpoint setzen bei der Methode die die REST Schnittstelle darstellt und die benötigten Informationen eintragen bei der Anfrage, um zu schauen wie Anfrage beim Spring Server ankommt.

## Informationen:

- HL7 CDS Hook Spezifkationsstandard: https://cds-hooks.hl7.org/
- CDS Hook Spezifikation Implementierung: https://cds-hooks.org/

