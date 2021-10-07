package com.camunda.example.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("starter")
public class ProcessStarterDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution exec) throws Exception {

        // TODO: Can execution be used to get BPMN error event details without transporting those through process data?
        BpmnError bpmnError = (BpmnError) Variables.objectValue(exec.getVariable("bpmnError"));

        log.info("errorCode: {} errorMessage {} failedAt {}", bpmnError.getErrorCode(), bpmnError.getMessage(), bpmnError.getCause());

        ObjectValue errorJson = Variables
                .objectValue(bpmnError)
                .serializationDataFormat(Variables.SerializationDataFormats.JSON)
                .create();

        exec.getProcessEngineServices().getRuntimeService().createMessageCorrelation("Message_StartErrorHandling")
                .setVariables(Map.of("bpmnError", errorJson));
    }
}
