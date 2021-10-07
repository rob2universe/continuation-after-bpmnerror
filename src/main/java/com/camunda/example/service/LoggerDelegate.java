/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
@Component("logger")
public class LoggerDelegate implements JavaDelegate {

    public void execute(DelegateExecution exec) {

        log.info("\n\n LoggerDelegate invoked by processDefinitionId: {}, activityId: {}, activityName: '{}'," +
                        " processInstanceId: {}, businessKey: {}, executionId: {}, modelName: {}, elementId: {} \n",
                exec.getProcessDefinitionId(),
                exec.getCurrentActivityId(),
                exec.getCurrentActivityName().replaceAll("\n", " "),
                exec.getProcessInstanceId(),
                exec.getProcessBusinessKey(),
                exec.getId(),
                exec.getBpmnModelInstance().getModel().getModelName(),
                exec.getBpmnModelElementInstance().getId()
        );

        log.info("--- Variables ---");
        Map<String, Object> variables = exec.getVariables();
        for (Map.Entry<String, Object> entry : variables.entrySet())
            log.info(entry.getKey() + " : " + entry.getValue());

        String activityId = exec.getCurrentActivityId();

        if (exec.getVariable("failAt").equals(activityId))
        {
          BpmnError bpmnError = new BpmnError("EC-00", activityId + ": Business Error occurred", new Throwable("activityId"));

          // Can execution be used to get BPMN error event details without transporting those through process data?
          ObjectValue errorJson = Variables
                .objectValue(bpmnError)
                .serializationDataFormat(Variables.SerializationDataFormats.JSON)
                .create();
          exec.setVariable("bpmnError", errorJson);
          throw bpmnError;
        }
    }

}