terraform {
  backend "s3" {
    bucket  = "tcp-ocp-tfstate"
    key     = "ocp-charlie"
    region  = "us-east-2"
  }
}

provider "aws" {
  region                  = "us-east-2"
}

resource "aws_db_instance" "sampledb" {
  allocated_storage    = 20
  engine               = "postgres"
  engine_version       = "11.5"
  storage_type         = "gp2"
  identifier_prefix    = "tcp-ocp-"
  instance_class       = "db.t2.micro"
  name                 = "app"
  username             = "foo"
  password             = "changeme"
}

# This has to exist here AND in boostrap so it doesn't get destroyed on apply
resource "aws_s3_bucket" "terraform_state" {
  bucket = "tcp-ocp-tfstate"

  versioning {
    enabled = true
  }

  lifecycle {
    prevent_destroy = true
  }
}
