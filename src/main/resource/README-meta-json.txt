### 
### This file describes the JSON structure of meta resources.
###

-- Overview

Meta resource files are named using the following filename pattern"<classname>-meta.json".
When a @MetaResource annotated class is loaded the resource is looked up and loaded. 
The JSON resource file has the structure which mimics that in-code @Meta annotations. The
JSON resources are loaded first on a per object/class basis and then any in-code annotations
are applied as overrides to the JSON resources. This allows in-code annotations to
override resources and definitions for Meta Objects.  

1) All packet, header and individual header fields can be represented as "meta" element.

2) To turn one of these to a meta object, annotate it with @Meta annotation. 
   @Meta annotation can be applied to java class, a class field or class method.

3) Additionally, you can specify the following further meta related annotations:
  - @Display: adds display properties such as a label and a format value which is used
              to generate displayable text. Additionally @Display annotation is repeatable.
              You can specify a @Display annotation for each Display level.
              
  - @Resolver: resolver provides a algorithmic way to convert the raw value to a human
               readable text value. For example there are predefined resolves for IP address
               to host name lookup, as well as protocol value to protocol name lookups etc..
               
Any annotation can be first defined as a JSON resource, which allows the actual annotation
to be omitted from in-code usage. The only exception is that you always need to provide
the @Meta annotation, but its values can be defined in JSON resource file and/or overridden
with usage of the @Meta annotation attributes in code.

If you do not specify a @Meta annotation on a class element (TYPE, FIELD or METHOD) it will
not be defined as a meta object, even if the JSON resource for the element exists. This 
ensures an explicit but minimalistic in-code declaration of all meta resources.

-- JSON meta resource file structure

JSON resource file is a JSON compliant source file (no comments allowed) that defines
various sections (JsonObject types) and arrays of values which are loaded and applied to
meta objects as if they were provided via in-code annotations. Actual in-code annotations
override the values with JSON resource files allowing overrides from in-code annotations to 
take precedent over JSON definitions.

The basic format is as follows (in pseudo definition):

{
	/* Summary line which is displayed by certain formats such as: */ 
	"display": "field1=%{field1:R} (raw value=%{}d)",
	
	/* Class level @Meta annotation section
	"meta": {},
	
	/* A per meta field @Meta annotation within the class (class FIELD or METHOD)*/ 
	"field": {
	
		/* A single field @Meta annotation */
		"field1": {
		
			/* field1's @Meta annotation definition */
			"meta": {},
			
			/* or for @Meta(ordinal = 10) single usage, in compact mode */
			"meta": 10,
			
			/* field1's multiple @Display annotation definitions per Detail level 
			"display": {
				/* @Display(detail = Detail.LOW) display level definition */
				"LOW": {},
				
				/* @Display(detail = Detail.HIGH) display level definition */
				"HIGH": {},
				
				/* Alternatively you can use a special case DEFAULT to apply to all levels */
				"DEFAULT": {}
			},
			
			/* or to apply a single display to all Display levels, in compact mode */
			"display": "",
			
			/* Raw value to human readable lookups and resolvers, ie. DNS resolution */
			"resolvers": [
				TIMESTAMP
			],
			
			/* or for single value, array bracket can be omitted, in compact mode */
			"resolvers": TIMESTAMP
		}
	}
}

-- Detail levels

The @Display annotation provides a special meta format string which is used to generate
textual representation of the meta object. @Display.detail attribute can be used to
provide a different @Display annotation per every detail level. The following Detail enum
types are defined: LOW, MEDIUM, HIGH, DEBUG. Just like java Logger Levels display attributes
allows selection of differently detailed output types when combined with @Display annotation
on a meta element. For example fields can provide very terse and raw values for Detail.LOW
level while providing much more expanded output such as resolved IP addresses, etc for
Detail.HIGH level.

Here are some examples of how Detail levels work with @Display annotation (in JSON form):

1) "display": { "LOW": "low", "MEDIUM": "medium", "HIGH": "high" }
	- Detail.LOW outputs "low"
	- Detail.MEDIUM output "medium"
	- Detail.HIGH outputs "high"
	
2) "display": { "MEDIUM": "medium", "HIGH": "high" }
	- Detail.LOW produces no output
	- Detail.MEDIUM output "medium"
	- Detail.HIGH outputs "high"
	
3) "display": { "HIGH": "high" }
	- Detail.LOW produces no output
	- Detail.MEDIUM produces no output
	- Detail.HIGH outputs "high"
	
4) "display": { "LOW": "low", "HIGH": "high" }
	- Detail.LOW produces "low"
	- Detail.MEDIUM produces no output
	- Detail.HIGH outputs "high"
	
4) "display": { "DEFAULT": "default", "HIGH": "high" }
	- Detail.LOW produces "default"
	- Detail.MEDIUM produces "default"
	- Detail.HIGH outputs "high"


-- Locale

The meta resources a localized and can be specified for every language and region. By creating
a <class-json_us>.json resource files, the appropriate locale specific resource file will 
be loaded when available. 
  