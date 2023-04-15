# spring-cds-service-template

## Getting started:

Dieses Repository erlaubt einen direkt lauffähige Anwendung herunterzuladen. Hierbei erfordert es nur noch die jeweilige REST Schnittstelle im Interface "CdsServicesApi" anzupassen oder eine neue REST Schnittstelle hinzuzufügen.

## Beispiel REST Schnittstelle im Projekt:

Die Schnittstelle lässt sich über die URL http://localhost:8080/cds-services/template aufrufen und man stellt die Anfragetype auf POST ein.

Pflichtinhalte für die Anfrage zur Standard REST Schnittstelle:  
- Authorization -> Bearer Token vom FHIR Auth Server 
- Content-Type: application/json
- Body Inhalt: cds-hook-example-body.json

Einfach einen Breakpoint setzen bei der Methode die die REST Schnittstelle darstellt und die benötigten Informationen eintragen bei der Anfrage, um zu schauen wie Anfrage beim Spring Server ankommt.

## Informationen:

- HL7 CDS Hook Spezifkationsstandard: https://cds-hooks.hl7.org/
- CDS Hook Spezifikation Implementierung: https://cds-hooks.org/

## Download:

Nur diese Option wählen wenn man alles selbst machen und struggeln will.

 - Download: https://editor.swagger.io/?url=https://raw.githubusercontent.com/cds-hooks/api/master/cds-hooks.yaml   
 i. Oben findet sich das Tap "Generate Server"   
 ii. "Spring" auswählen   
 iii. Paket runterladen 
 - Leider funktioniert der Spring Server von der oben erwähnten Swagger Projekt nicht auf anhieb und es müssen noch einige Libs angepasst werden und Fehler gelöst werden.
