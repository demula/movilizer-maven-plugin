package com.movilizer.plugins.maven.services;


import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilizer.plugins.maven.DefaultValues;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.WriterFactory;
import org.codehaus.plexus.util.xml.XmlStreamWriter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@Singleton
public class MovilizerXMLParserService {
    private JAXBContext movilizerRequestContext;
    private Unmarshaller movilizerRequestUnmarshaller;
    private Marshaller movilizerRequestMarshaller;
    private Log mavenOutputLogger;
    private URL xmlOutputDir = getClass().getResource("resources/");

    @Inject
    public MovilizerXMLParserService(Log mavenOutputLogger, String xmlOutputDir) throws JAXBException {
        assert mavenOutputLogger != null;
        this.mavenOutputLogger = mavenOutputLogger;
        if (xmlOutputDir != null) this.xmlOutputDir = getClass().getResource(xmlOutputDir);

        movilizerRequestContext = JAXBContext.newInstance(MovilizerRequest.class);
        movilizerRequestMarshaller = movilizerRequestContext.createMarshaller();
        movilizerRequestUnmarshaller = movilizerRequestContext.createUnmarshaller();
//        ValidationEventHandler validationEventHandler = new javax.xml.bind.helpers.DefaultValidationEventHandler();
        movilizerRequestUnmarshaller.setEventHandler(new ValidationEventHandler() {
            @Override
            public boolean handleEvent(ValidationEvent event) {
                mavenOutputLogger.error("Error unmarshalling Movilizer request: " + event.getMessage());
                return true;
            }
        });
    }

    public MovilizerRequest getRequestFromFile(String filePath) throws IOException, URISyntaxException, JAXBException {
        URL fullPath = getClass().getResource(filePath);
        if (fullPath == null) throw new IOException(String.format("File not found for path %s", filePath));
        return getRequestFromFile(new File(fullPath.toURI()));
    }

    public MovilizerRequest getRequestFromFile(File file) throws JAXBException {
        JAXBElement<MovilizerRequest> root;
        root = movilizerRequestUnmarshaller.unmarshal(new StreamSource(file), MovilizerRequest.class);
        return root.getValue();
    }

    public void saveRequestToFile(MovilizerRequest request, String fileName) throws IOException, URISyntaxException, JAXBException {
        File newFile = new File(xmlOutputDir + fileName + DefaultValues.MOVILIZER_XML_EXTENSION);
        newFile.getParentFile().mkdirs();
        saveRequestToFile(request, newFile);
    }

    public void saveRequestToFile(MovilizerRequest request, File file) throws IOException, JAXBException {
        file.getParentFile().mkdirs();
        XmlStreamWriter xmlWriter = WriterFactory.newXmlWriter(file);
        movilizerRequestMarshaller.marshal(new JAXBElement<>(new QName("uri","local"), MovilizerRequest.class, request), xmlWriter);
    }
}
