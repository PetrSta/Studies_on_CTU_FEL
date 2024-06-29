#!/usr/bin/env bash
saveOutput=0
errorLineFound=0
filesToSave=()
output=output.tgz
while getopts "hz" opt; do
	case "${opt}" in
		h) 
			echo "Script has two different switches -h & -z which saves the output into archive. 
			It prints out information about requsted file path depending on file type etc."
			exit 0
			;;
		z) 
			saveOutput=1
			;;
		?) 
			echo "Error wrong switch invoked." >&2
			exit 2
			;;
	esac
done
while read -r Line; do
	if [ "$?" -ne 1 ];
	then
		if [[ "${Line:0:5}" == "PATH " ]]; 
		then
			validInputs=2
			pathIdentified=0
			pathToFile="${Line#PATH }"
			if [ -L "$pathToFile" ] && [ "$pathIdentified" -ne 1 ]; 
			then
				targetFile=$(readlink "$pathToFile")
				validInputs=1
				pathIdentified=1
				echo "LINK '${pathToFile}' '${targetFile}'"
			fi
			if [ ! -e "$pathToFile" ] && [ "$pathIdentified" -ne 1 ];
			then
				errorLineFound=1
				pathIdentified=1
				echo "ERROR '$pathToFile'" >&2
			fi
			if [ -f "$pathToFile" ] && [ "$pathIdentified" -ne 1 ]; 
			then
				if [ ! -r "$pathToFile" ];
				then
					echo "Error cannot read file"
					exit 2
				fi
				numberOfLines=$(wc -l <"$pathToFile")
				read -r firstLine<"$pathToFile" 	
				validInputs=1
				pathIdentified=1
				if [ "$saveOutput" -eq 1 ]; 
				then
					filesToSave+=("$pathToFile")
				fi
				echo "FILE '${pathToFile}' ${numberOfLines} '${firstLine}'"
			fi
			if [ -d "$pathToFile" ] && [ "$pathIdentified" -ne 1 ]; 
			then 
				validInputs=1
				pathIdentified=1
				echo "DIR '${pathToFile}'"
			fi

			if [ "$validInputs" -eq 0 ]; 
			then
				echo "ERROR '${pathToFile}'" >&2
				errorLineFound=1
			fi
		fi
	else
		echo "Error encountered while reading input"
		exit 2
	fi
done
if [ "$saveOutput" -eq 1 ]; 
then
	if [ "${#filesToSave[*]}" -ne 0 ];
	then
		tar czf "$output" "${filesToSave[@]}"
		if [ ! -w "$output" ]
		then
			echo "Error cannot write output"
			exit 2
		fi
	fi
fi
if [ "$errorLineFound" -eq 1 ]; 
then
	exit 1
else 
	exit 0
fi