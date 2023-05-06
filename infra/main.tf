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
  name          = "lc-vinilemess-go-calc"
  security_groups = ["sg-812ea5f2"]
  key_name = "kp-devops-vinilemess"
}

resource "aws_elb" "elb-vinilemess-go-calc" {
  security_groups = ["sg-812ea5f2"]
  name = "elb-vinilemess-go-calc"
  availability_zones = var.AVAILABILITY_ZONES

  listener {
    instance_port     = 8080
    instance_protocol = "http"
    lb_port           = 80
    lb_protocol       = "http"
  }

  health_check {
    healthy_threshold   = 2
    interval            = 30
    target              = "HTTP:8080/health"
    timeout             = 5
    unhealthy_threshold = 2
  }

  tags = var.TAGS
}

resource "aws_autoscaling_group" "asg-vinilemess-go-calc" {
  desired_capacity = 1
  max_size = 1
  min_size = 1
  launch_configuration = aws_launch_configuration.lc-go-calc.name
  load_balancers = [aws_elb.elb-vinilemess-go-calc.name]
  availability_zones = var.AVAILABILITY_ZONES


