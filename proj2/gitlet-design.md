# Gitlet Design Document

**Name**: Carter

## Classes and Data Structures
A Java field is a variable inside a class. For instance, in a class representing an employee, the Employee class might contain the following fields: name. position. salary.

### Class BranchPointer

#### Fields

String name

String pointedID



### Class stagedForAdditionArea

#### Fields

TreeMap<String, String (Blob)> stagedAddFiles(can stage multiple files)



### Class stagedForAdditionArea

#### Fields

TreeMap<String, String (Blob)> stagedRemovalFiles(can stage multiple files)



### Class Repository

(don't use it to do persistence job , unless you need to resave it every time you make a change to the repo)

#### Fields 

1. TreeMap<String, String (Commit)>  branches  {branchName: Commit Object}

   default: master 

2. String HEAD 

   

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

![image-20220616095503633](https://s2.loli.net/2022/06/16/ZFA4dMbu5X9KLyR.png)

merge demands:





current branch, given branch:

Firstly,  one branch is at the split point: (Step1: find the split point commit)

algorithm:

1)use BFS to find the shortest path to each ancestor commit from each branch; O(N) times to iterate over all the ancestor commit  (2N)

2)find all the ancestor commit distance for one branch ,then use tree map to save the distance from ancestor commit to the branch commit, and use contains method to determine if this commit is the common ancestor; O(logN ) to determine if the common ancestor commit Sets contains this commit;

3)pick the latest commit in the common ancestor commit Sets;



1. If the split point *is* the same commit as the given branch, then we do nothing. Operation ends with the message `Given branch is an ancestor of the current branch.`
2.  If the split point is the current branch, check out the given branch, printing the message `Current branch fast-forwarded.`

Secondly, both branches aren't at the split point, Then any files ...

1. have been *modified* in the given branch since the split point, but not modified in the current branch since the split point should be changed to their versions in the given branch (checked out from the commit at the front of the given branch). These files should then all be automatically staged. 

   

2.  have been modified in the current branch but not in the given branch, do nothing;

3. have been modified in both the current and given branch in the same way (have the same content or were both removed), left unchanged by the merge.

   PS: if a file was removed from both the current and given branch, but a file of the same name is present in the working directory, it is left alone and continues to be absent (not tracked nor staged) in the merge.

   

4.  not present at the split point, and present only in the current branch, remain as they are.

5. not present at the split point and are present only in the given branch should be checked out and staged.

   Take current branch as granted:

6.  present at the split point, unmodified in the current branch, and absent in the given branch should be removed (and untracked)

7.  present at the split point, unmodified in the given branch, and absent in the current branch should remain absent

   

8.  modified in different ways in the current and given branches are *in conflict*：

   1） contents of both are changed and different from other

   2）the contents of one are changed and the other file is deleted

   3）the file was absent at the split point and has different contents in the given and current branches

   In this case, replace the contents of the conflicted file with and stage the result.


![image-20220623110514702](E:\course_projects\cs61b\proj2\image-20220623110514702.png)


### Class Commit

#### Fields

timestamp for initial commit 00:00:00 UTC, Thursday, 1 January 1970 

1. String message

2. String timestamp 

3. TreeMap<String, String (Blob)> blobs   {name: Blob}

4. Commit parent

   

### Class Blob 

#### Fields

1.String fileName

2.String blobID (sha1-hash)

3.String blobContent;







### Class GitletException

#### Fields

1. Field 1
2. Field 2

## Algorithms











## Persistence

The directory structure looks like this:

CWD

------ .gitlet

-------------- ~~repo (the repo object serialized and saved here)~~

--------------objects/

------------------------6a 

----------------------------6a13(commits)

-----------------------------6ab4(blob)



----------------HEAD (save the ref == references, eg:   ref/heads/main or dev)  (main's contents: the hash id for a commit);

-----------------refs/

-----------------------heads/

-----------------------branchName1(eg:main)

-----------------------branchName2(eg:dev)



-----------------indexForAddition

-----------------indexForRemoval









--------------

![image-20220616095524209](https://s2.loli.net/2022/06/16/Ct8F9DAVWhJXpm2.png)

## 

## Persistence strategy:

## Key Idea: Give yourself persistence “for free”.

- Try to abstract away persistence as much as possible. 

1. For each *thing* that needs to persist, write a helper method that will load it from your file system and a helper method that will save it to your file system.
2. You should never have to worry about how or where something is loaded or saved when you are implementing your gitlet commands. * 
3. Get familiar with the concept of lazy loading and caching.



4.commit all in one folder: objects/commits/....  (consider the blob also don't split to different folders)

all commit file name is just the full sha1-hash id, not split to different folders, because need to iterate over all the commits when implement global-log and find command;



consider convert blob object to a just file (write blob content to the file, don't save the name as an instance variable) -- to achieve this, such as in staging area, you need to sha1(staged file's content)  to address the exist blob file;
