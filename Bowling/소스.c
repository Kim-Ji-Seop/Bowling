#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include <ctype.h>
#include <windows.h>

#define _CRT_SECURE_NO_WARNINGS
#pragma warning(disable : 4996)
typedef struct Member {
	int member_num;
	int game_round;
	int score;
	int frameSum;
}Bowling_crew;

Bowling_crew crew[125] = { NULL };
int z = 0;
int frame_sum[125] = { 0, };
double average(int sum) {
	return (double)(sum) / 5.0;
}
int totalsum(int *frame,int frameNum) {
	int sum = 0;
	for (int i = 0; i < frameNum; i++) {
		printf("%d ", frame[i]);
		sum += frame[i];
	}
	printf("\n");
	return sum;
}
int Bowling_Score(int* arr, int data) {
	int f_score = 0;
	int frameScore;
	int j = 0;
	int frame[10] = { 0, };
	int is_strike[10] = { 0, };
	int is_spare[10] = { 0, };
	int score;
	
	int frame_8_9_10 = 0;
	for (int i = 0; i < data; i++) {
		if (j > 9) {
			break;
		}
		frameScore = 0;
		score = arr[i];
		frameScore += score;
		frame[j] += score;
		if (j - 2 >= 0 && is_strike[j - 2] > 0) {
			is_strike[j - 2] -= 1;
			frame[j - 2] += score;
		}
		if (j - 1 >= 0 && is_strike[j - 1] > 0) {
			is_strike[j - 1] -= 1;
			frame[j - 1] += score;
		}
		if (j - 1 >= 0 && is_spare[j - 1] > 0) {
			is_spare[j - 1] -= 1;
			frame[j - 1] += score;
		}
		if (score == 10) {
			is_strike[j] += 2;
			j++;//다음 프레임으로 이동
			continue;
		}
		i += 1;
		score = arr[i];
		frameScore += score;
		frame[j] += score;
		if (j - 1 >= 0 && is_strike[j - 1] > 0) {
			is_strike[j - 1] -= 1;
			frame[j - 1] += score;
		}
		if (frameScore == 10) {
			is_spare[j] += 1;
			j++;//다음 프레임으로 이동
			continue;
		}
		else {
			j++;
			continue;
		}

	}
	if (is_strike[9]) {
		score = arr[data - 2];
		if (is_strike[8]) {
			frame[8] += score;
			is_strike[8] -= 1;
		}
		frame[9] += score;
		score = arr[data-1];
		frame[9] += score;
	}
	else if (is_spare[9]) {
		score = arr[data - 1];
		frame[9] += score;
	}
	f_score = totalsum(frame, 10);
	
	frame_8_9_10 = frame[7] + frame[8] + frame[9];
	crew[z].frameSum = frame_8_9_10;
	return f_score;
	
}
void Split(char* arr)
{
	char* tk;
	int member[2];
	int frame_num[24] = { NULL };
	int i = 1;
	int member_score;

	tk = strtok(arr, " \t");
	
	member[0] = atoi(tk);
	crew[z].member_num = member[0];
	tk = strtok(NULL, " \t");

	member[1] = atoi(tk);
	crew[z].game_round = member[1];
	printf("%d %d\n", member[0],member[1]);
	tk = strtok(NULL, " \t");
	while (tk != NULL) {
		if (atoi(tk) != 10) {
			frame_num[i] = atoi(tk);
		}
		else {
			frame_num[i] = atoi(tk);
		}
		
		tk = strtok(NULL, " \t\n");
		i++;
	}
	printf("\n");
	int* str = malloc(sizeof(int) * i);
	printf("\n");
	for (int j = 0; j < i-1 ; j++) {
		str[j] = frame_num[j + 1];
		printf("%d ", str[j]);
	}
	printf("\n");
	printf("최종점수: %d ",  member_score = Bowling_Score(str, i - 1));
	crew[z].score = member_score;
	printf("\n");
	z++;
	free(str);
	
}

void test() {
	FILE* fp = fopen("bowling2018.txt", "r");
	char buf[100] = { NULL };
	int i = 0;
	int member,round;
	int frame[12] = { 0 };
	Bowling_crew tmp;
	int x, j;
	double avg_tmp;
	while (!feof(fp)) {
		if (fgets(buf, 100, fp) != NULL) {
			Split(buf);//데이터 불러온 뒤  점수처리 
		}
	}
	//오름차순정렬(구조체 멤버)
	for (i = 0; i < 125; i++) {	
		for (j = 0; j < 124 - i; j++) {
			if (crew[j].member_num > crew[j + 1].member_num) {
				tmp = crew[j];
				crew[j] = crew[j + 1];
				crew[j + 1] = tmp;
			}
		}
	}
	
	
	int sum;
	int avg_index = 0;
	int* memberNum = malloc(sizeof(int) * 25);
	double* avg = malloc(sizeof(double) * 25);
	Bowling_crew frameSum_tmp;

	for (i = 0; i < 125; i+=5) {
		sum = 0;
		for (j = i; j < i+5; j++) {
			sum += crew[j].score;
		}
		avg[avg_index] = average(sum);
		memberNum[avg_index] = avg_index + 1;
		avg_index++;
	}
	for (i = 0; i < 125; i++) {
		for (j = 0; j < 124 - i; j++) {
			if (crew[j].frameSum > crew[j + 1].frameSum) {
				frameSum_tmp = crew[j];
				crew[j] = crew[j + 1];
				crew[j + 1] = frameSum_tmp;
			}
		}
	}
	
	for (i = 0; i < 25; i++) {
		//printf("%d번의 5게임 평균 : %.1lf\n ", memberNum[i], avg[i]);
	}
	int member_tmp;
	for (i = 0; i < 25; i++) {
		for (j = 0; j < 24 - i; j++) {
			if (avg[j] > avg[j + 1]) {
				avg_tmp = avg[j];
				avg[j] = avg[j + 1];
				avg[j + 1] = avg_tmp;

				member_tmp = memberNum[j];
				memberNum[j] = memberNum[j + 1];
				memberNum[j + 1] = member_tmp;
			}
		}
	}
	printf("\n");
	printf("2) 5게임의 평균 점수가 1, 2, 3위인 멤버고유번호와 점수(소수 첫째자리까지 표시)는 각각 얼마인가?\n");
	for (i = 24; i > 0; i--) {
		printf("%d번의 5게임 평균 : %.1lf\n ", memberNum[i], avg[i]);
	}
	printf("\n");
	printf("3) 8, 9, 10 마지막 세 프레임의 점수합계가 가장 높은 것과 낮은 것은 각각 어느 멤버의 몇번째 게임이고, 점수는 얼마인가?\n");
	for (i = 0; i < 125; i++) {
		printf("%d번 %d라운드 점수: %d / 8,9,10프레임 합계: %d\n", crew[i].member_num, crew[i].game_round, crew[i].score, crew[i].frameSum);
	}
	
	free(memberNum);
	free(avg);
	fclose(fp);
}
int main() {
	test();
}
