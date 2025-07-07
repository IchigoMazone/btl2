package org.example.Test;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Requests")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestXML {

    @XmlElement(name = "Request")
    private List<org.example.Test.Request> requests = new ArrayList<>();

    public List<org.example.Test.Request> getRequests() {
        return requests;
    }

    public void setRequests(List<org.example.Test.Request> requests) {
        this.requests = requests;
    }
}

