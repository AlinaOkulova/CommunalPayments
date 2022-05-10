package com.example.communalpayments.services.interfaces;

import com.example.communalpayments.entities.Template;
import com.example.communalpayments.exceptions.AddressNotFoundException;

import java.util.List;

public interface TemplateService {

    List<Template> getAllTemplatesByAddressId(Long addressId) throws AddressNotFoundException;
}
