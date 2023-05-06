terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

variable "AWS_ACCESS_KEY" {
  type = string
}
variable "AWS_SECRET_ACCESS_KEY" {
  type = string
}

variable "AVAILABILITY_ZONES" {
  default = [
    "us-east-1a",
    "us-east-1b",
    "us-east-1c",
    "us-east-1d",
    "us-east-1e",
    "us-east-1f"
  ]
}

provider "aws" {
  access_key = var.AWS_ACCESS_KEY
  secret_key = var.AWS_SECRET_ACCESS_KEY
  region     = "us-east-1"
}

resource "aws_launch_configuration" "lc-go-calc" {
  instance_type = "t2.micro"
  image_id      = "ami-0af8f2a1b353c9aec"
  name          = "lc-fintrack-api"
  security_groups = ["sg-812ea5f2"]
  key_name = "kp-devops-vinilemess"
}


