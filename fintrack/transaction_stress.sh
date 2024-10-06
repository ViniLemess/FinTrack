#!/bin/bash

# Check if the user provided the number of transactions and users as arguments
if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Usage: $0 <number_of_transactions> <number_of_users> <initial_amount>"
  exit 1
fi

# Number of transactions
num_transactions=$1

# Number of users (parallel processes)
num_users=$2

# Initial amount
initial_amount=$3

# API endpoint
api_url="http://localhost:8080/transaction"

# Function to create a transaction
create_transaction() {
  local start=$1
  local end=$2
  for (( i=start; i<=end; i++ )); do
    local amount=$(echo "$initial_amount + $i * 10.55" | bc)
    local transaction_payload=$(cat <<EOF
{
  "amount": $amount,
  "description": "new transfer received.",
  "type": "INCOME"
}
EOF
    )
    # Capture the HTTP status code and suppress the output
    http_status=$(curl -s -o /dev/null -w "%{http_code}" -X POST $api_url \
      -H "Content-Type: application/json" \
      -d "$transaction_payload")

    # Check for successful response
    if [ "$http_status" -ne 201 ]; then
      echo "Error: Request failed with status $http_status"
    fi
  done
}

# Calculate the number of transactions per user
transactions_per_user=$((num_transactions / num_users))
remainder=$((num_transactions % num_users))

start=0
end=0

# Run the transactions in parallel
for (( user=1; user<=num_users; user++ )); do
  end=$((start + transactions_per_user - 1))

  # Distribute the remainder among users
  if (( remainder > 0 )); then
    end=$((end + 1))
    remainder=$((remainder - 1))
  fi

  create_transaction $start $end &
  start=$((end + 1))
done

# Wait for all parallel processes to finish
wait

echo "Completed $num_transactions transactions with $num_users users in parallel."