package org.example.Request;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Requests")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestXML {

    @XmlElement(name = "Request")
    private List<Request> requests = new ArrayList<>();

    public List<Request> getRequests() {
        return requests;
    }
    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
