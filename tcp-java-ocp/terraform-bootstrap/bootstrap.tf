provider "aws" {
  region                  = "us-east-2"
}
resource "aws_s3_bucket" "terraform_state" {
  bucket = "tcp-ocp-tfstate"

  versioning {
    enabled = true
  }

  lifecycle {
    prevent_destroy = true
  }
}
