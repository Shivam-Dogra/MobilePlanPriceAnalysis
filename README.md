# Mobile Plan Price Analysis - Backend

## Project Overview

The **Mobile Plan Price Analysis** backend project, developed using Spring Boot and Java, focuses on analyzing and comparing mobile plan prices from various service providers. It includes features such as web scraping, data processing, search frequency, page ranking, and API endpoints for seamless integration with front-end applications.

## Technologies Used

- **Java 8+**
- **Spring Boot**
- **Selenium WebDriver**
- **Jsoup**
- **Maven**
- **JUnit (for testing)**

## Installation

To set up the backend locally, follow these steps:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Shivam-Dogra/MobilePlanPriceAnalysis.git
   ```
2. **Navigate to the project directory:**
   ```bash
   cd MobilePlanPriceAnalysis
   ```
3. **Build the project using Maven:**
   ```bash
   mvn clean install
   ```
4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

## Usage

After setting up the backend, you can use it to perform various tasks:

1. **Web Scraping:**
   - Scrape mobile plan data from specified websites.
   - The scraped data includes plan names, monthly costs, data allowances, network coverage, and additional features.

2. **API Endpoints:**
   - Access the collected data through RESTful APIs.
   - Integrate these APIs with front-end applications for data presentation and analysis.

3. **Search and Ranking:**
   - Perform search frequency and page ranking operations using the provided algorithms.

## Key Components and Algorithms

### 1. **Web Scraping Service**
   - **Classes:** `WebScrapingService`, `MobilePlan`
   - **Algorithms Used:**
     - **List:** Stores mobile plan objects retrieved from the website.
     - **WebDriver (Selenium):** Automates web browser interactions to scrape data.
     - **WebElement (Selenium):** Extracts specific elements from the web pages for data retrieval.
   - **Features Implemented:**
     - Scrapes data from Bell, Rogers, and Freedom websites.
     - Retrieves mobile plan details like plan name, monthly cost, data allowance, network coverage, and call/text allowance.

### 2. **Search Frequency**
   - **Classes:** `SearchFrequencyService`
   - **Algorithms Used:**
     - **Binary Search Tree (BST):** Efficiently stores words and their frequencies, allowing for quick insertion and retrieval.
     - **File I/O Operations:** Verifies if a given word exists in the dictionary, ensuring only valid words are inserted into the BST.
   - **Features Implemented:**
     - Tracks and retrieves the frequency of keywords in the text data.
     - Uses a BST to manage word insertion and frequency tracking.

### 3. **Page Ranking**
   - **Classes:** `PageRankingService`
   - **Algorithms Used:**
     - **Priority Queue:** Stores web pages based on their scores, enabling efficient retrieval of top-ranked pages.
     - **HashMap:** Tracks the frequency of each keyword found in web pages, crucial for calculating page scores.
   - **Features Implemented:**
     - Ranks web pages based on keyword matches and their frequencies.
     - Retrieves the top-ranked web pages according to calculated scores.

### 4. **Data Validation**
   - **Classes:** `DataValidationService`
   - **Algorithms Used:**
     - **Regular Expressions (Regex):** Validates word formats, search queries, URLs, keywords, and word lists to ensure they adhere to specific criteria.
   - **Features Implemented:**
     - Validates input data formats across various functionalities, ensuring integrity and usability.
     - Ensures that only valid data is processed by the system.

### 5. **Inverted Indexing**
   - **Classes:** `InvertedIndexService`
   - **Algorithms Used:**
     - **AVL Tree:** A self-balancing binary search tree used for efficient search, insertion, and deletion of indexed words.
   - **Features Implemented:**
     - Builds an inverted index of words from the scraped data.
     - Facilitates fast keyword search operations across the indexed data.

### 6. **Word Completion**
   - **Classes:** `WordCompletionService`
   - **Algorithms Used:**
     - **Trie (Prefix Tree):** Efficiently stores and retrieves a set of strings, supporting operations like insertion, search, and word completion.
     - **Depth-First Search (DFS):** Traverses the Trie structure during word completion, exploring all possible paths to find word suggestions.
   - **Features Implemented:**
     - Provides word completion suggestions based on a given prefix.
     - Enhances user experience in tasks like spell checking or word prediction.

### 7. **HTML Parser**
   - **Classes:** `HTMLParserService`
   - **Algorithms Used:**
     - **Jsoup Document and Elements:** Parses and manipulates HTML content, extracting relevant data from web pages.
   - **Features Implemented:**
     - Parses HTML content from specified URLs.
     - Extracts and processes data for use in other components like web scraping and data validation.

## Contributing

Contributions to the project are welcome. To contribute:

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -m 'Add feature-name'`
4. Push to the branch: `git push origin feature-name`
5. Open a pull request.

