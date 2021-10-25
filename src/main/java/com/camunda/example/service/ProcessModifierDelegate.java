package com.camunda.example.service;

import com.camunda.example.dto.BusinessError;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("modifier")
public class ProcessModifierDelegate implements JavaDelegate {

  @Override
  public void execute(DelegateExecution exec) {

    ObjectValue errorJson = exec.getVariableTyped("businessError");
    BusinessError businessError = (BusinessError) errorJson.getValue();

    exec.getProcessEngineServices().getRuntimeService()
        .createProcessInstanceModification(businessError.getProcessInstance())
        .cancellationSourceExternal(true)
        .cancelAllForActivity("ErrorHandlingCompletedEvent")
        .startBeforeActivity(businessError.getActivityId())
        //clear data to simulate error has been resolved
        .setVariable("failAt", "")
        .execute();
  }
}
