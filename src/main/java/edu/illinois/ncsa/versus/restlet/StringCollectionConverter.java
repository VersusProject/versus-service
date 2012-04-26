/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgement if the
 * software is used.
 */
package edu.illinois.ncsa.versus.restlet;

import java.util.Collection;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 *
 * @author antoinev
 */
public abstract class StringCollectionConverter<T extends Collection<String>>
        implements Converter {

    abstract protected String getNodeName();

    abstract protected T getNewT();

    @Override
    abstract public boolean canConvert(Class type);

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        T collection = (T) source;

        for (String element : collection) {
            writer.startNode(getNodeName());
            writer.setValue(element);
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        T result = getNewT();

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if (getNodeName().equals(reader.getNodeName())) {
                result.add(reader.getValue());
            } else {
                throw new ConversionException("Unexpected node '"
                        + reader.getNodeName() + "'");
            }
            reader.moveUp();
        }
        return result;
    }
}
