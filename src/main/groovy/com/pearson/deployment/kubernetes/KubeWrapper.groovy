package com.pearson.deployment.kubernetes

import com.pearson.deployment.AnsiColors

class KubeWrapper {
  String klass
  String namespace

  KubeWrapper(String klass, String namespace) {
    this.klass = klass
    this.namespace = namespace
  }

  def get(def name) {
    exe("kubectl get ${klass} ${name} --namespace=${namespace} -o yaml")
  }

  def create(def filename) {
    exe("kubectl create -f ${filename} --namespace=${namespace}")
  }

  def apply(def filename) {
    exe("kubectl apply -f ${filename} --namespace=${namespace}")
  }

  def exe(cmd) {
    Process command
    def c = appendShellPrefix(cmd)
    command = c.execute()
    command.waitFor()
    def errOutput = command.err.text
    if (errOutput) {
      println("${AnsiColors.red}${errOutput}${AnsiColors.reset}")
      throw new Exception("Error executing '${cmd}'")
    }
    return command.text
  }

  private def appendShellPrefix(String cmd) {
    String[] commandArray = new String[3]
    commandArray[0] = "sh"
    commandArray[1] = "-c"
    commandArray[2] = cmd
    return commandArray
  }
}