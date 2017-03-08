# polydata-insights

Service for collectiong insights on different poly records

Insights are stored by tenant and type collections

Each document in collection will contain key, value, date, custom data

Example :

Collection: cats.votes

| _id  | key  | value  | date  | custom data  |
|---|---|---|---|---|
| x  |  cat1  |  5 |  17.01.2017  |  {}  |
| x  | cat1  | 1  |  18.02.2017 |  {}  |
| x  |  cat2  | 5  | 22.02.2017  | {}  |

## Example queries
 
 - get top keys by value in time range
 - get top keys by count in time range
 - get values for key in time range : 5 start = > 1, 1 star = 10
 - get average for key in time range
 - get voted with specific value

Get insights grouped by sum of values x

```
db['test_tenant.test_insight'].aggregate(
[
	{ $match: { date: { $gte: new ISODate("2017-02-21") } }},
	{$group: { _id: "$key", total: { $sum: "$value"  }	     }   },
	{ $sort: { total: -1 } }
]

)


{ "_id" : "test_insight", "total" : NumberLong(64)}
{ "_id" : "test_insight2", "total" : NumberLong(2)}

```

Fetching votes per key
```
db['test_tenant.test_insight'].aggregate(
[
	{ $match: {  key:"test_insight2", date: { $gte: new ISODate("2017-02-21") } }},
	{$group: { _id: "$key", keys: { $push: { value: "$value" } }	     }   }

]

)

{ "_id" : "test_insight2", "keys" : [ { "value" : { "$numberLong" : "2" } } ] }
```

Group keys by insight count
```
db['test_tenant.test_insight'].aggregate(
[
	{ $match: { date: { $gte: new ISODate("2017-02-21") } }},
	{$group: { _id: "$key", count: { $sum: 1  }	     }   },
	{ $sort: { count: -1 } }
]
)


{  "_id" : "test_insight",  "count" : NumberInt(34) }
{  "_id" : "test_insight2", "count" : NumberInt(1) }
```

Average value  x
```

db['test_tenant.test_insight'].aggregate(
[
	{ $match: { date: { $gte: new ISODate("2017-02-21") } }},
	{$group: { _id: "$key", avg: { $avg: "$value"  }	     }   },
	{ $sort: { avg: -1 } }
]

)

```


## Example API calls

http://localhost:9100/swagger-ui.html

Log Insight

Request

```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "key": "potato-wallpaper", \ 
   "tenant": "test_tenant", \ 
   "type": "test_insight_type", \ 
   "value": "1" \ 
 }' 'http://localhost:9100/api/insight'
```

Get insights by sum value
 

Request

```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "insight": "test_insight_type", \ 
   "interval": "DAY", \ 
   "tenant": "test_tenant" \ 
 }' 'http://localhost:9100/api/insight/value/sum'
```

Response

```
{
  "version": "0.0.1",
  "data": [
    {
      "count": 8,
      "_id": "test_insight"
    },
    {
      "count": 6,
      "_id": "potato-wallpaper"
    }
  ]
}
```

Average statistics

Request

```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "insight": "test_insight_type", \ 
   "interval": "DAY", \ 
   "tenant": "test_tenant" \ 
 }' 'http://localhost:9100/api/insight/value/average'
```

Response

```
{
  "version": "0.0.1",
  "data": [
    {
      "avg": 2,
      "_id": "test_insight"
    },
    {
      "avg": 1,
      "_id": "potato-wallpaper"
    }
  ]
}
```

Fetch poly insights

Request

```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/hal+json' -d '{ \ 
   "insight": "test_insight_type", \ 
   "interval": "DAY", \ 
   "tenant": "test_tenant", \ 
 "key" :"potato-wallpaper" \ 
 }' 'http://localhost:9100/api/insight/key'
```

Response
```
{
  "version": "0.0.1",
  "data": [
    {
      "stats": {
        "1": 6
      },
      "_id": "potato-wallpaper"
    }
  ]
}
```

License
=======
 
    Copyright (c) 2017 Denis O <denis.o@linux.com>
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
