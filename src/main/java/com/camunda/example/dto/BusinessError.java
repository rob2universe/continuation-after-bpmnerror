package com.camunda.example.dto;

public class BusinessError {
  private String processInstance;
  private String activityInstance;
  private String activityId;
  private String errorMessage;
  private String errorCode;

  public String getProcessInstance() {return this.processInstance;}

  public String getActivityInstance() {return this.activityInstance;}

  public String getActivityId() {return this.activityId;}

  public String getErrorMessage() {return this.errorMessage;}

  public String getErrorCode() {return this.errorCode;}

  public void setProcessInstance(String processInstance) {this.processInstance = processInstance;}

  public void setActivityInstance(String activityInstance) {this.activityInstance = activityInstance;}

  public void setActivityId(String activityId) {this.activityId = activityId;}

  public void setErrorMessage(String errorMessage) {this.errorMessage = errorMessage;}

  public void setErrorCode(String errorCode) {this.errorCode = errorCode;}

  public String toString() {return "BusinessError(processInstance=" + this.getProcessInstance() + ", activityInstance=" + this.getActivityInstance() + ", activityId=" + this.getActivityId() + ", errorMessage=" + this.getErrorMessage() + ", errorCode=" + this.getErrorCode() + ")";}
}
