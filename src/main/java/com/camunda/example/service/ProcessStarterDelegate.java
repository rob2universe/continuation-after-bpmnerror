package com.camunda.example.service;

import com.camunda.example.dto.BusinessError;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("starter")
public class ProcessStarterDelegate implements JavaDelegate {

  @Override
  public void execute(DelegateExecution exec) {

    exec.getProcessEngineServices().getRuntimeService().createMessageCorrelation("Message_StartErrorHandling")
        .setVariables(Map.of("businessError", exec.getVariable("businessError")))
        .correlate();
  }
}
