# Gitlet Design Document

**Carter**:

## Classes and Data Structures
A Java field is a variable inside a class. For instance, in a class representing an employee, the Employee class might contain the following fields: name. position. salary.

### Class Repository
#### Fields 

1. TreeMap<String, Commit>  branches  {branchName: Commit Object}

   default: master 

2. Commit HEAD 

3. TreeMap<String, Blob> stagedForAddition (can stage multiple files)

4. TreeMap<String, Blob> stagedForRemoval(can stage multiple files)

   

5. modifications   (extra credit)

3. untrackedFiles (extra credit)



#### methods

init



add

 only one file may be added at a time.



commit 

A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.

By default a commit has the same file contents as its parent. 

- Each commit is identified by its SHA-1 id, which must include the file (blob) references of its files, parent reference, log message, and commit time.
- **Differences from real git**: In real git, commits may have multiple parents (due to merging) and also have considerably more metadata.



rm

- If the file is neither staged nor tracked by the head commit, print the error message `No reason to remove the file.`



log

- following the first parent commit links, ignoring any second parents found in merge commits.



global-log

- The order of the commits does not matter.



find

- same hint as global-log, iterate over commits use the utils' method



checkout

-  If a working file is untracked in the current branch and would be overwritten by the checkout, print `There is an untracked file in the way; delete it, or add and commit it first.` and exit; perform this check before doing anything else. Do not change the CWD.



branch 

- This command does NOT immediately switch to the newly created branch (just as in real Git).

  

reset

- Note that in Gitlet, there is no way to be in a detached head state since there is no `checkout` command that will move the HEAD pointer to a specific commit. The `reset` command will do that, though it also moves the branch pointer. Thus, in Gitlet, you will never be in a detached HEAD state.

![image-20220603183413248](C:\Users\Carter\AppData\Roaming\Typora\typora-user-images\image-20220603183413248.png)

merge

 




### Class Commit

#### Fields

timestamp for initial commit 00:00:00 UTC, Thursday, 1 January 1970 

1. message

1. commitID

3. commitTime

4. blobs (TreeMap<String, String>)  {name: file's sha1-hash}

5. mergedParent

   

### Class GitletException

#### Fields

1. Field 1
2. Field 2

## Algorithms











## Persistence

The directory structure looks like this:

CWD

------ .gitlet

--------------repo(the repo object serialized and saved here)

--------------objects

------------------------6a 

----------------------------6a13(commits)

-----------------------------6ab4(blob)







