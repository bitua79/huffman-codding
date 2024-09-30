# Huffman Coding Project

## Overview
This project implements the Huffman coding algorithm for lossless data compression. It encodes a given text file into a binary string and decodes the binary file back to the original text. The encoding and decoding programs are separated to ensure modularity.

## Functionality
- **Encoding**: Reads a text file, calculates the frequency of each character, builds a priority queue, generates the Huffman tree, and encodes the text into a binary string. The binary string is stored in a binary file with a header containing the character frequencies and their corresponding Huffman codes.
- **Decoding**: Reads the binary file, retrieves the header information, reconstructs the Huffman tree, and decodes the binary string back into the original text file.

## Implementation Details
- **Frequency Calculation**: The program calculates the frequency of each character in the input text using an efficient *O(n)* algorithm.
- **Priority Queue**: A priority queue is constructed with characters sorted by frequency, where the character with the lowest frequency is at the front.
- **Huffman Tree**: The Huffman tree is generated based on the priority queue. The tree nodes represent characters and their corresponding frequencies.
- **Canonical Huffman Code**: The Huffman codes are converted to canonical form to minimize overhead in the binary header.
- **Header Information**: The header of the binary file includes details about the Huffman tree and character codes, allowing the decoding program to reconstruct the original data without needing direct access to the tree.

## Notes
- Ensure the input text file is in the correct format and accessible.
- The binary output file will contain a header that includes character frequencies and Huffman codes.
- The implementation separates encoding and decoding logic, allowing for easy modifications and enhancements.

## References
[Huffman Coding Algorithm](https://en.wikipedia.org/wiki/Huffman_coding)
[Canonical Huffman Coding](https://en.wikipedia.org/wiki/Huffman_coding#Canonical_Huffman_code)
