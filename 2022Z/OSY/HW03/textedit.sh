#!/usr/bin/env bash
workDone=0
while getopts "habcd:" opt; do
	case "${opt}" in
		h) 
            echo "Script has 3 switches -a (pdf files print), -b (print lines with number at the bigging without that number) and -c (finds sentences in input)"
			exit 0
			;;
		a) 
			if [ "$workDone" -eq 0 ];
            then
                dir=$(pwd)
                #add all files in working directory to variable
                allFiles=$(ls -a "$dir")
                #checks if file and with pdf and if so prints it
                for file in $allFiles
                do
                    echo "${file}" | grep '.*\.[pP][dD][fF]$'
                done
            fi
            workDone=1
			;;
		b) 
            if [ "$workDone" -eq 0 ];
            then
                grep '^[+-]\?[1-9]\+' | sed 's|^[+-]\?[1-9]\+||'
            fi
            workDone=1
			;;
        c)
            if [ "$workDone" -eq 0 ];
            then
                #replaces all end of lines with one space, then replace either . or ? or ! with what was found and newline 
                #and finaly removes all spaces at the bigginning of line
                sentenceLine=$(cat | tr '\n' ' ' | sed 's/[.?!]/&\n/g' | sed 's/^ //g')
                #finds sentence patters -> lines that start with capital letter and end with either of these: [.?!]
                echo "$sentenceLine" | grep '[[:upper:]].*[.?!]$'
            fi
            workDone=1
            ;;
        d)
            prefix=${OPTARG}
            sed "s|\([[:space:]]*#[[:space:]]*include[[:space:]]*[\"]\)\(.*\.h[\"]\)|\1${prefix}\2|g" | sed "s|\([[:space:]]*#[[:space:]]*include[[:space:]]*[<]\)\(.*\.h[>]\)|\1${prefix}\2|g"
            ;;
        ?)
            echo "Script has 3 switches -a (pdf files print), -b (print lines with number at the bigging without that number) and -c (finds sentences in input)"
            exit 1
			;;
	esac
done
exit 0