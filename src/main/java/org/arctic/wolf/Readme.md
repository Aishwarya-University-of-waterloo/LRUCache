# In-Memory Cache Implementation

## Introduction
This Java project implements an in-memory data structure acting as a cache. The cache is designed to store key-value pairs with generic types and provides features such as bounded size, time-based invalidation, and eviction based on least frequency usage.

## Requirements
- The cache must be bound in size to a maximum value.
- Keys and values must be generic types.
- Entries in the cache must be invalidated after a given amount of time.
- Entries in the cache must be evicted by least frequency usage.
- Test-cases for the cache should be defined and called in the `main()` function.

## Implementation
The project includes the following components:
- `Cache` interface: Defines the contract for the cache implementation.
- `LFUCache` class: Implements the `Cache` interface and provides the LFU (Least Frequently Used) cache functionality.
- `Solution` class: Contains the `main()` method to execute test cases for the cache.

## Usage
To use the cache implementation:
1. Import the Java classes into your project.
2. Create an instance of `LFUCache` and configure it according to your requirements.
3. Use the cache methods (`put`, `get`, etc.) to interact with the cache.

## Test Cases
The project includes test cases to validate the functionality of the cache implementation. These test cases cover various scenarios, including cache size limitation, time-based invalidation, and eviction based on least frequency usage.

## Dependencies
- Java (version 8.X.X)

## How to Run
To run the test cases:
1. Compile the Java files.
2. Execute the `main()` method in the `Solution` class.


