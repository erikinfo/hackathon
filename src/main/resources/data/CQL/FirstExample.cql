library FirstExample

using FHIR version '4.0.1'

//rules:

//Criteria 1
context ResearchStudy
define InclusionCriteria1:
  exists (grp in context.group |
    grp.characteristic.code.coding.exists(coding.code = 'C25696') and
    grp.characteristic.valueCodeableConcept.coding.exists(coding.code = 'Adenocarcinoma') and
    grp.characteristic.exclude = false)