package com.samagra.gupshup.incoming;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.samagra.Publisher.CommonProducer;
import com.samagra.adapter.cdac.CdacBulkSmsAdapter;
import com.samagra.adapter.cdac.TrackDetails;
import com.samagra.adapter.provider.factory.ProviderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/campaign")
public class Campaign {
    @Value("${campaign}")
    private String campaign;

    @Autowired
    public CommonProducer kafkaProducer;

    @Autowired
    private ProviderFactory factoryProvider;

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void startCampaign(@RequestParam("campaignId") String campaignId) throws JsonProcessingException, JAXBException {
        kafkaProducer.send(campaign, campaignId);
        return;
    }

    @RequestMapping(value = "/pause", method = RequestMethod.GET)
    public void pauseCampaign(@RequestParam("campaignId") String campaignId) throws JsonProcessingException, JAXBException {
        kafkaProducer.send(campaign, campaignId);
        return;
    }

    @RequestMapping(value = "/resume", method = RequestMethod.GET)
    public void resumeCampaign(@RequestParam("campaignId") String campaignId) throws JsonProcessingException, JAXBException {
        kafkaProducer.send(campaign, campaignId);
        return;
    }

    @RequestMapping(value = "/status/cdac/bulk", method = RequestMethod.GET)
    public TrackDetails getCampaignStatus(@RequestParam("campaignId") String campaignId) {
        CdacBulkSmsAdapter iprovider = (CdacBulkSmsAdapter) factoryProvider.getProvider("cdac", "SMS");
        try {
            return iprovider.getLastTrackingReport(campaignId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}