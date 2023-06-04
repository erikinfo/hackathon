library FirstExample

using FHIR version '4.0.1'

//rules:

//Criteria 1
context Patient
define InclusionCriteria1:
  exists Extension.valueCodeableConcept.coding
    where Extension.url = 'http://hl7.org/fhir/StructureDefinition/research-study-inclusion-criterion' and
          Extension.valueCodeableConcept.coding.system = 'http://example.com/codes' and
          Extension.valueCodeableConcept.coding.code = 'C25696'

// Criterion 2
context Patient
define InclusionCriterion2:
  exists Extension.valueCodeableConcept.coding
          Extension.url = 'http://hl7.org/fhir/StructureDefinition/research-study-inclusion-criterion' and
          Extension.valueCodeableConcept.coding.system = 'http://example.com/codes' and
          Extension.valueCodeableConcept.coding.code = 'C25696'

// Criterion 3
context Patient
define InclusionCriterion3:
  exists Extension.valueCodeableConcept.coding
    where Extension.url = 'http://hl7.org/fhir/StructureDefinition/research-study-inclusion-criterion' and
          Extension.valueCodeableConcept.coding.system = 'http://example.com/codes' and
          Extension.valueCodeableConcept.coding.code = 'C39434'

//other criterias to be implemented later

//query
context Patient
define PatientQuery:
 InclusionCriteria1 and InclusionCriterion2 and InclusionCriterion3



