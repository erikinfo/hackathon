



codesystem "LOINC": ‘http://loinc.org’

valueset “Lung Cancer": ‘http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1116.89'
valueset “Condition Clinical Status": ‘http: //utah.edu/fhir/1cs-cds/ValueSet/conditionclinical’

code “Tobacco Smoking Status”: °72166-2' from “LOINC”

context Patient

define “Smoking status observation":
(Observation: “Tobacco Smoking Status”] 0
where O.status in { ‘final’, ‘amended’ }

define “Lung cancer diagnosis”:
[Condition: “Lung Cancer”] C
where C.clinicalStatus in “Condition Clinical status"