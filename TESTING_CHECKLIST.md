# Contract Watch — Testing Checklist

Use this checklist before the jury/demo.

## 1. Start the application

- Run `ContractWatchApplication` in IntelliJ IDEA.
- Open `http://localhost:8080`.
- Confirm the dashboard loads.

## 2. Create a vendor

POST `/api/vendors`

```json
{
  "name": "ABC Services",
  "contactEmail": "vendor@example.com",
  "contactPhone": "+91 9876543210",
  "notes": "Primary maintenance vendor"
}
```

Expected: HTTP 201 and a vendor ID.

## 3. Create a contract

Use the vendor ID from step 2.

POST `/api/contracts`

```json
{
  "vendorId": 1,
  "serviceName": "Annual equipment maintenance",
  "contractStartDate": "2026-01-01",
  "endDate": "2026-12-31",
  "renewalNoticePeriodDays": 60,
  "contactEmail": "vendor@example.com",
  "status": "ACTIVE",
  "notes": "Review pricing before renewal"
}
```

Expected: HTTP 201.

## 4. Test the renewal boundary rule

For an end date of `2026-12-31` and a 60-day notice period, the review boundary is `2026-11-01`.

On `2026-10-31` the contract should not be flagged.

On `2026-11-01` the contract should be flagged for renewal review.

Use GET `/api/contracts/renewal-review` to verify the flag.

## 5. Test expiring contracts

GET `/api/contracts/expiring?days=30`

Only ACTIVE contracts whose end dates fall between today and today + 30 days should appear.

## 6. Test renewal decision — RENEWED

POST `/api/contracts/1/renewal-decision`

```json
{
  "decision": "RENEWED",
  "newEndDate": "2027-12-31",
  "notes": "Renewed after annual review"
}
```

Expected:

- Decision history contains `RENEWED`.
- Contract remains `ACTIVE`.
- Contract end date changes to `2027-12-31`.
- Renewal review flag is recalculated using the new end date.

## 7. Test renewal decision — TERMINATED

POST `/api/contracts/1/renewal-decision`

```json
{
  "decision": "TERMINATED",
  "newEndDate": null,
  "notes": "Service no longer required"
}
```

Expected:

- Decision history contains `TERMINATED`.
- Contract status becomes `TERMINATED`.
- Renewal review flag becomes false.
- Contract does not appear in `/api/contracts/expiring?days=30`.
- Contract does not appear in `/api/contracts/renewal-review`.

## 8. Test invalid dates

Try to create a contract where the end date is before the start date.

Expected: HTTP 400 with a clear error message.

## 9. Test invalid notice period

Try a notice period below 0 or above 3650.

Expected: HTTP 400.

## 10. Test renewed contract without a new end date

Send `RENEWED` with `newEndDate` as null.

Expected: HTTP 400.

## 11. Test document references

POST `/api/contracts/1/documents`

```json
{
  "title": "Signed Agreement",
  "referenceUrl": "https://example.com/contracts/agreement.pdf",
  "description": "Signed contract reference"
}
```

Then GET `/api/contracts/1/documents`.

Expected: the reference is visible and can be opened from the dashboard.

## 12. Dashboard

GET `/api/dashboard`

Confirm:

- `totalContracts`
- `activeContracts`
- `expiringIn30Days`
- `renewalReviewContracts`
- `asOfDate`

## 13. Scheduler

The scheduler runs every day at 9:00 AM using the server's configured timezone.

For a classroom demo, it is not necessary to wait for 9:00 AM. You can call the service/API manually and inspect the application logs, or temporarily change the cron expression during development and restore it before submission.

SMTP is optional. Without SMTP configuration, the scheduler logs reminder activity instead of sending email.
