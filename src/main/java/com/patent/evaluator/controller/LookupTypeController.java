package com.patent.evaluator.controller;


import com.patent.evaluator.constant.SuccessMessages;
import com.patent.evaluator.domain.lookuptype.LookupType;
import com.patent.evaluator.domain.lookuptype.LookupValue;
import com.patent.evaluator.dto.SuccessResponseDto;
import com.patent.evaluator.dto.lookuptype.LookupTypeDto;
import com.patent.evaluator.dto.lookuptype.LookupValueDto;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import com.patent.evaluator.service.api.lookuptype.LookupTypeRules;
import com.patent.evaluator.service.api.lookuptype.LookupValueRules;
import com.patent.evaluator.service.impl.lookuptype.LookupTypeMapper;
import com.patent.evaluator.service.impl.lookuptype.LookupValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("rest/lookupType")
public class LookupTypeController {

    private LookupTypeRules lookupTypeRules;
    private LookupValueRules lookupValueRules;

    @PostMapping()
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity saveType(@Valid @RequestBody LookupTypeDto lookupTypeDto) {
        lookupTypeRules.save(lookupTypeDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_CREATE_TITLE, SuccessMessages.GENERIC_TYPE_CREATE_MESSAGE), HttpStatus.OK);
    }

    @PostMapping("value")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity saveValue(@Valid @RequestBody LookupValueDto lookupValueDto) {
        lookupValueRules.save(lookupValueDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_VALUE_CREATE_TITLE, SuccessMessages.GENERIC_TYPE_VALUE_CREATE_MESSAGE), HttpStatus.OK);
    }

    @PutMapping(value = "{lookupTypeId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity updateType(@PathVariable Integer lookupTypeId, @Valid @RequestBody LookupTypeDto lookupTypeDto) {
        lookupTypeRules.update(lookupTypeId, lookupTypeDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_UPDATE_TITLE, SuccessMessages.GENERIC_TYPE_UPDATE_MESSAGE), HttpStatus.OK);
    }

    @PutMapping(value = "value/{lookupValueId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity updateValue(@PathVariable Integer lookupValueId, @Valid @RequestBody String value) {
        lookupValueRules.update(lookupValueId, value);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_VALUE_UPDATE_TITLE, SuccessMessages.GENERIC_TYPE_VALUE_UPDATE_MESSAGE), HttpStatus.OK);
    }

    @GetMapping(value = "value/{lookupValueId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity readValue(@PathVariable Integer lookupValueId) {
        LookupValue lookupValue = lookupValueRules.read(lookupValueId);
        LookupValueDto lookupValueDto = LookupValueMapper.INSTANCE.entityToDto(lookupValue);
        return new ResponseEntity<>(lookupValueDto, HttpStatus.OK);
    }

    @GetMapping(value = "{lookupTypeId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity readType(@PathVariable Integer lookupTypeId) {
        LookupType lookupType = lookupTypeRules.read(lookupTypeId);
        LookupTypeDto lookupTypeDto = LookupTypeMapper.INSTANCE.entityToDto(lookupType);
        return new ResponseEntity<>(lookupTypeDto, HttpStatus.OK);
    }

    @GetMapping("getAll")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity readType() {
        List<LookupType> lookupTypes = lookupTypeRules.readAll();
        List<LookupTypeDto> lookupTypeDtos = LookupTypeMapper.INSTANCE.entityListToDtoList(lookupTypes);
        return new ResponseEntity<>(lookupTypeDtos, HttpStatus.OK);
    }

    @PostMapping("list")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity<Page<LookupTypeDto>> getLookupTypePage(@RequestBody PageableSearchFilterDto pageRequest) {
        Page<LookupTypeDto> response = lookupTypeRules.getAll(pageRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("values/{lookupTypeId}/getAll")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity readTypeValues(@PathVariable Integer lookupTypeId) {
        List<LookupValueDto> lookupValueDtos = LookupValueMapper.INSTANCE.entityListToDtoList(lookupValueRules.readAllByLookupTypeId(lookupTypeId));
        return new ResponseEntity<>(lookupValueDtos, HttpStatus.OK);
    }

    @GetMapping("values/getIcons")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getIcons() {
        List<LookupValueDto> lookupValueDtos = LookupValueMapper.INSTANCE.entityListToDtoList(lookupValueRules.readAllIcons());
        return new ResponseEntity<>(lookupValueDtos, HttpStatus.OK);
    }

    @DeleteMapping("value/{lookupValueId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity changeActiveOfLookupValue(@PathVariable Integer lookupValueId) {
        this.lookupValueRules.changeActive(lookupValueId);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_VALUE_UPDATE_TITLE, SuccessMessages.GENERIC_TYPE_VALUE_UPDATE_MESSAGE), HttpStatus.OK);
    }

    @Autowired
    public void setLookupTypeRules(LookupTypeRules lookupTypeRules) {
        this.lookupTypeRules = lookupTypeRules;
    }

    @Autowired
    public void setLookupValueRules(LookupValueRules lookupValueRules) {
        this.lookupValueRules = lookupValueRules;
    }
}

