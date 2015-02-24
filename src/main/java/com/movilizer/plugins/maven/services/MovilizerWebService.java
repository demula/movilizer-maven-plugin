package com.movilizer.plugins.maven.services;

import com.movilitas.movilizer.v12.*;
import com.movilizer.plugins.maven.messages.EN;
import org.apache.maven.plugin.logging.Log;

import javax.xml.ws.BindingProvider;

public class MovilizerWebService {
    private final Log mavenOutputLogger;
    private MovilizerWebServiceV12 movilizerCloud;

    public MovilizerWebService(final Log mavenOutputLogger, String webServiceAddress) {
        this.mavenOutputLogger = mavenOutputLogger;
        MovilizerWebServiceV12Service service = new MovilizerWebServiceV12Service();
        movilizerCloud = service.getMovilizerWebServiceV12Soap11();
        BindingProvider provider = (BindingProvider) movilizerCloud;
        provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webServiceAddress);
    }

    public MovilizerRequest prepareRequest(Long systemId, String password, MovilizerRequest request) {
        // Load system credentials
        request.setSystemId(systemId);
        request.setSystemPassword(password);
        // Set default values for single use mode
        request.setNumResponses(0);
        request.setUseAutoAcknowledge(false);
        request.setResponseSize(0);
        return request;
    }

    public MovilizerResponse getReplyFromCloud(MovilizerRequest request) {
        MovilizerResponse response = movilizerCloud.movilizer(request);
        mavenOutputLogger.debug(String.format("Received response from movilizer cloud with system id %d", response.getSystemId()));
        return response;
    }

    public boolean responesHasErrors(MovilizerResponse response) {
        if (!response.getDocumentError().isEmpty()) return true;
        if (!response.getMasterdataError().isEmpty()) return true;
        if (!response.getMoveletError().isEmpty()) return true;
        if (!response.getStatusMessage().isEmpty()) {
            for (MovilizerStatusMessage message : response.getStatusMessage()) {
                if (403 == message.getType()) { //System id failed to authenticate
                    return true;
                }
            }
        }
        return false;
    }

    public String prettyPrintErrors(MovilizerResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append(EN.RESPONSE_HAS_ERRORS).append("\n");
        if (!response.getDocumentError().isEmpty()) {
            sb.append(EN.DOCUMENT_ERRORS)
                    .append(" (")
                    .append(String.valueOf(response.getDocumentError().size()))
                    .append(")\n");
            for (MovilizerDocumentError error : response.getDocumentError()) {
                sb.append("  - ")
                        .append(error.getValidationErrorCode())
                        .append(": ")
                        .append(error.getMessage())
                        .append("\n");
            }
        }
        if (!response.getMasterdataError().isEmpty()) {
            sb.append(EN.MASTERDATA_ERRORS)
                    .append(" (")
                    .append(String.valueOf(response.getMasterdataError().size()))
                    .append(")\n");
            for (MovilizerMasterdataError error : response.getMasterdataError()) {
                sb.append("  - ")
                        .append(error.getValidationErrorCode())
                        .append(": ")
                        .append(error.getMessage())
                        .append("\n");
            }
        }
        if (!response.getMoveletError().isEmpty()) {
            sb.append(EN.MOVELET_ERRORS)
                    .append(" (")
                    .append(String.valueOf(response.getMoveletError().size()))
                    .append(")\n");
            for (MovilizerMoveletError error : response.getMoveletError()) {
                sb.append("  - ")
                        .append(error.getValidationErrorCode())
                        .append(": ")
                        .append(error.getMessage())
                        .append("\n");
            }
        }
        if (!response.getStatusMessage().isEmpty()) {
            for (MovilizerStatusMessage message : response.getStatusMessage()) {
                if (403 == message.getType()) { //System id failed to authenticate
                    sb.append(EN.AUTH_ERROR)
                            .append("\n")
                            .append("  - ")
                            .append(EN.INCORRECT_CREDENTIALS)
                            .append("'")
                            .append(response.getSystemId())
                            .append("'");
                }
            }
        }
        return sb.toString();
    }
}
