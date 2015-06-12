# Experimental OrgRef API

The API follows the [json:api](http://jsonapi.org/) specification. The
use of the `application/vnd.api+json` content type is optional (if sent in the
`Accept` header it will be returned, otherwise the generic `application/json`
will be used).


## GET /organizations

Retrieve all organizations, optionally limited by some criteria.

**URL parameters:**

* **name** - name of the organization, will perform a partial match against all names
* **country** - country using two-letter ISO code (e.g., "US")
* **state** - US state using two-letter code (e.g., "NY")
* **level** - level in the tree, supported are: "Org", "Grp", or "Sub"
* **wikipedia** - Wikipedia URL for the organization
* **wikidata** - Wikidata URL for the organization
* **viaf** - VIAF URL for the organization
* **isni** -ISNI for the organization, may be the URL or the ISNI itself (e.g., "http://www.isni.org/isni/0000000419368200" or "0000000419368200")

**Responses:**

* 200 OK - list of Organizations that match the specified parameters

**Example Response:***

```json
{
  "data": [
    {
      "id": 1859,
      "type": "organization",
      "attributes": {
        "viaf": "http://viaf.org/viaf/123652612",
        "name": "Arizona State University",
        "isni": "http://www.isni.org/isni/0000000121512636",
        "wikipedia": "http://en.wikipedia.org/wiki?curid=1859",
        "state": "AZ",
        "level": "Org",
        "website": "http://www.asu.edu",
        "wikidata": "http://www.wikidata.org/wiki/Q670897",
        "country": "US"
      }
    }
    ...
  ]
}
```

## GET /organizations/:id

Retrieve a single organization by its identifier.

**Responses:**

* 200 OK - Organization matching the ID requested
* 404 Not Found

**Example Response:**

```json
{
  "data": {
    "id": 62866,
    "type": "organization",
    "attributes": {
      "viaf": "http://viaf.org/viaf/131327464",
      "name": "United States Department of Energy",
      "isni": "http://www.isni.org/isni/0000000412615133",
      "wikipedia": "http://en.wikipedia.org/wiki?curid=62866",
      "state": "DC",
      "level": "Org",
      "website": "http://www.energy.gov",
      "wikidata": "http://www.wikidata.org/wiki/Q217810",
      "country": "US"
    }
  }
}
```
