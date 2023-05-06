
variable "aws_access_key" {
  type    = string
}

variable "aws_secret_key" {
  type    = string
}

source "amazon-ebs" "ami-fintrack" {
  access_key           = "${var.aws_access_key}"
  secret_key           = "${var.aws_secret_key}"
  ami_name             = "ami-fintrack-api"
  instance_type        = "t2.micro"
  region               = "us-east-1"
  source_ami           = "ami-007855ac798b5175e"
  ssh_keypair_name     = "kp-fintrack-vinilemess"
  ssh_private_key_file = "/mnt/c/Users/pcnov/Downloads/kp-fintrack-vinilemess.pem"
  ssh_username         = "ec2-user"
}

build {
  sources = ["source.amazon-ebs.ami-fintrack"]

  provisioner "shell" {
   script = "${file("./install_docker.sh")}"
}
