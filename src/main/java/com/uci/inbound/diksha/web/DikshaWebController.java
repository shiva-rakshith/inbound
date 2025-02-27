package com.uci.inbound.diksha.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uci.adapter.sunbird.web.SunbirdWebPortalAdapter;
import com.uci.adapter.sunbird.web.inbound.DikshaWebMessageFormat;
import com.uci.inbound.utils.XMsgProcessingUtil;
import com.uci.dao.repository.XMessageRepository;
import com.uci.utils.BotService;
import com.uci.utils.CommonProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;

@Slf4j
@RestController
@RequestMapping(value = "/diksha")
public class DikshaWebController {

    @Value("${inboundProcessed}")
    private String inboundProcessed;

    @Value("${inbound-error}")
    private String inboundError;

    private SunbirdWebPortalAdapter sunbirdWebPortalAdapter;

    @Autowired
    public CommonProducer kafkaProducer;

    @Autowired
    public XMessageRepository xmsgRepo;

    @Autowired
    public BotService botService;

    @RequestMapping(value = "/web", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void dikshaWeb(@RequestBody DikshaWebMessageFormat message) throws JsonProcessingException, JAXBException {

        System.out.println(message.toString());

        sunbirdWebPortalAdapter = SunbirdWebPortalAdapter.builder()
                .build();

        XMsgProcessingUtil.builder()
                .adapter(sunbirdWebPortalAdapter)
                .xMsgRepo(xmsgRepo)
                .inboundMessage(message.getMessages()[0])
                .topicFailure(inboundError)
                .topicSuccess(inboundProcessed)
                .kafkaProducer(kafkaProducer)
                .build()
                .process();
    }
}
