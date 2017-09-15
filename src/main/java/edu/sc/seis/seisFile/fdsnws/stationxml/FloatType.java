package edu.sc.seis.seisFile.fdsnws.stationxml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import edu.sc.seis.seisFile.fdsnws.StaxUtil;
import edu.sc.seis.seisFile.fdsnws.stationxml.StationXMLException;
import edu.sc.seis.seisFile.fdsnws.stationxml.StationXMLTagNames;


/** a float value with optional unit and errors. */
public class FloatType extends FloatNoUnitType {

    // for hibernate
    FloatType() {
        
    }
    
    public FloatType(float value, String unit, Float plusError, Float minusError) {
        super(value, plusError, minusError);
        this.unit = unit;
    }

    public FloatType(float value, String unit) {
    	    this(value, unit, null, null);
    }
    
    public FloatType(XMLEventReader reader, String tagName) throws StationXMLException, XMLStreamException {
        this(reader, tagName, null);
    }

    public FloatType(XMLEventReader reader, String tagName, String fixedUnit) throws StationXMLException, XMLStreamException {
        StartElement startE = StaxUtil.expectStartElement(tagName, reader);
        super.parseAttributes(startE);
        super.parseValue(reader);
        parseUnitAttr(startE, fixedUnit);
    }
    
    void parseUnitAttr(StartElement startE, String fixedUnit) throws StationXMLException {
        String unitStr = StaxUtil.pullAttributeIfExists(startE, StationXMLTagNames.UNIT);
        if (unitStr != null) {
            unit = unitStr;
        } else {
            unit = fixedUnit;
        }
    }
    
    public String toString() {
        String out = ""+getValue();
        if (hasPlusError() || hasMinusError()) {
            out += "(";
            if (hasPlusError()) {
                out += "+"+getPlusError();
            }
            if (hasMinusError()) {
                if (hasPlusError()) {
                    out += " ";
                }
                out += getMinusError();
            }
            out += ")";
        }
        return out;
    }
    
    
    public String getUnit() {
        return unit;
    }

    void setUnit(String u) {
        this.unit = u;
    }
    
    String unit;
}
