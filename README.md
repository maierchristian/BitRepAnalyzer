# BitRepAnalyzer
Software to analyze a Bitcoin Address and get a Reputation Score

To run the Java project yourself install the following things:
      - Node.js Runtime
      - npm
      - Blocktrail SDK via npm  (https://www.npmjs.com/package/blocktrail-sdk)
      
When this is done you need to create an account at the blocktrail website (https://www.blocktrail.com/). 
With that you can generate an API Key and Secret pair, which you need in the programm.

Last step is to change the line <artifactId>j2v8_macosx_x86_64</artifactId> in the pom.xml file to your own operating 
system.

Windows: j2v8_win32_x86_64

macOS:   j2v8_macosx_x86_64

Linux:   j2v8_linux_x86_64
