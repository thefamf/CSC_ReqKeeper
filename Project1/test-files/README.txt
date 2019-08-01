This file contains a listing of test files in the test_files directory.

req1.xml - Valid req file
req2.xml - Negative id
req3.xml - Missing state
req4.xml - Missing summary
req5.xml - Missing test
req6.xml - File doesn't exist
req7.xml - Accepted with no estimate
req8.xml - Working with no estimate
req9.xml - Completed with no estimate
req10.xml - Verified with no estimate
req11.xml - Submitted with non-zero priority
req12.xml - Working with no developer
req13.xml - Completed with no developer
req14.xml - Verified with no developer
req15.xml - Rejected with no rejection reason
req16.xml - Accepted with no priority
req17.xml - Working with no priority
req18.xml - Completed with no priority
req19.xml - Verified with no priority
req20.xml - Rejected with non-zero priority
req21.xml - Accepted with priority 4


NOTE: Testing with XML files and using string comparisons of the contents 
can be very fragile.  For the RequirementsWriterTest, there can be no tabs
in the expected output files.  For both RequirementsWriterTest and 
RequirementsReaderTest, the summary contents must be on one line to minimize 
test failures due to new lines, tabs, or spaces.