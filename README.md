# poly-insights

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

Get insights grouped by sum of values

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
